package help.sausage.ui;

import static help.sausage.ui.component.ReviewCardComponent.default_review;
import static help.sausage.ui.data.Review.fromDto;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import help.sausage.client.ReviewClient;
import help.sausage.client.UserClient;
import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import help.sausage.entity.UserIcon;
import help.sausage.ui.component.ReviewCardComponent;
import help.sausage.ui.component.ReviewFormComponent;
import help.sausage.ui.component.SearchBox;
import help.sausage.ui.data.Review;
import help.sausage.ui.data.SessionUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

/*
<div>Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
 */
@Route
@CssImport("./styles/main-view.css")
public class MainView extends VerticalLayout implements ReviewFormComponent.ReviewCreatedListener {
    private static final long serialVersionUID = 42L;

    private final VerticalLayout reviewHolder = new VerticalLayout();

    private final ReviewClient reviewClient;
    private SessionUser user;

    public MainView(@Autowired ReviewClient reviewClient) {
        this.reviewClient = reviewClient;

        user = VaadinSession.getCurrent().getAttribute(SessionUser.class);
        if (user == null) {
            Notification.show("No user logged in");
            UI.getCurrent().navigate("login");
            return;
        }
        VerticalLayout centerColumn = new VerticalLayout();
        centerColumn.setWidth("640px");
        centerColumn.getStyle().set("padding", "0px");


        ReviewFormComponent buildForm = new ReviewFormComponent(reviewClient);
        reviewHolder.setWidth("100%");
        reviewHolder.setPadding(false);

        ResponseEntity<List<ReviewDto>> resp = reviewClient.getAllReviewsPaginated();
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            Notification.show("Error while retrieving reviews: " + resp.toString());
        } else {
            resp.getBody().forEach(
                    review -> reviewHolder.add(new ReviewCardComponent(fromDto(review))));
        }

        centerColumn.add(buildForm, reviewHolder);
        VerticalLayout left = leftColumn();
        VerticalLayout right = userSection();
        right.setWidth("200px");
        left.setWidth("200px");
        HorizontalLayout wrapper = new HorizontalLayout(left, centerColumn, right);
        wrapper.expand(centerColumn);
        setHorizontalComponentAlignment(Alignment.CENTER, wrapper);
        add(wrapper);
    }

    private VerticalLayout leftColumn() {
        SearchBox searchBox = new SearchBox(Notification::show);
        VerticalLayout layout = new VerticalLayout(searchBox, new H2("Home"));
        return layout;
    }

    private VerticalLayout userSection() {
        HorizontalLayout userLayout = new HorizontalLayout();
        userLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        H2 username = new H2(user.username());
        UserIcon userIcon = UserIcon.from(user.icon());
        Image avatar = new Image(userIcon.asUrl(), userIcon.name());
        avatar.setClassName("main-user-avatar");
        userLayout.add(avatar, username);

        Anchor logout = new Anchor("/logout", "logout");
        VerticalLayout layout = new VerticalLayout();
        layout.add(userLayout, logout);
        layout.setHorizontalComponentAlignment(Alignment.END, logout);
        return layout;
    }

    @Override
    public void onNewReview(ReviewDto reviewDto) {
        Review review = fromDto(reviewDto);
        reviewHolder.add(new ReviewCardComponent(review));
    }
}