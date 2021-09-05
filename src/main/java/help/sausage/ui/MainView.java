package help.sausage.ui;

import static help.sausage.ui.data.Review.fromDto;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import help.sausage.client.ReviewClient;
import help.sausage.dto.ReviewDto;
import help.sausage.entity.UserIcon;
import help.sausage.ui.component.LeftColumnComponent;
import help.sausage.ui.component.ReviewCardComponent;
import help.sausage.ui.component.ReviewFormComponent;
import help.sausage.ui.data.SessionUser;
import help.sausage.utils.ApplicationContextProvider;
import java.util.List;
import org.springframework.http.ResponseEntity;

/*
<div>Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
 */
@Route
@CssImport("./styles/main-view.css")
@PageTitle("Home | Sausage")
@StyleSheet("http://fonts.googleapis.com/css?family=Cabin+Sketch")
public class MainView extends VerticalLayout implements ReviewFormComponent.ReviewCreatedListener {
    private static final long serialVersionUID = 42L;

    private final VerticalLayout reviewHolder = new VerticalLayout();
    private final ReviewClient reviewClient;

    private SessionUser user;

    public MainView() {
        this.reviewClient = ApplicationContextProvider.getCtx().getBean(ReviewClient.class);

        user = VaadinSession.getCurrent().getAttribute(SessionUser.class);
        if (user == null) {
            Notification.show("No user logged in");
            UI.getCurrent().navigate("login");
            return;
        }

        VerticalLayout centerColumn = new VerticalLayout();
        centerColumn.setClassName("main-view-center");

        ReviewFormComponent reviewForm = new ReviewFormComponent();
        reviewForm.addOnReviewCreatedListener(this);
        reviewForm.setClassName("main-review-form");
        reviewHolder.setPadding(false);

        ResponseEntity<List<ReviewDto>> resp = reviewClient.getAllReviewsPaginated();
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            Notification.show("Error while retrieving reviews: " + resp.toString());
        } else {
            resp.getBody().forEach(
                    review -> reviewHolder.add(new ReviewCardComponent(fromDto(review))));
        }

        centerColumn.add(reviewForm, reviewHolder);
        VerticalLayout left = new LeftColumnComponent();
        VerticalLayout right = userSection();
        left.setClassName("main-left-column");
        right.setClassName("main-right-column");

        HorizontalLayout wrapper = new HorizontalLayout(left, centerColumn, right);
        wrapper.expand(centerColumn);
        setHorizontalComponentAlignment(Alignment.CENTER, wrapper);
        add(wrapper);
    }

    private VerticalLayout userSection() {
        HorizontalLayout userLayout = new HorizontalLayout();
        userLayout.setClassName("main-right-user-layout");
        userLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        H2 username = new H2(user.username());
        UserIcon userIcon = UserIcon.from(user.icon());
        Image avatar = new Image(userIcon.asUrl(), userIcon.name());
        avatar.setClassName("main-user-avatar");
        userLayout.add(avatar, username);

        VerticalLayout layout = new VerticalLayout();
        layout.add(userLayout);
        return layout;
    }

    @Override
    public void onNewReview(ReviewDto reviewDto) {
        UI.getCurrent().getPage().reload();
//        Review review = fromDto(reviewDto);
//        List<Component> components;
//        try {
//            ResponseEntity<List<ReviewDto>> reviews = reviewClient.getAllReviewsPaginated();
//            components = reviews.getBody()
//                    .stream().map(r -> (Component) new ReviewCardComponent(review)).toList();
//        } catch (Exception e) {
//            Notification.show("Could not load new reviews");
//            components = reviewHolder.getChildren().toList();
//        }
//        reviewHolder.removeAll();
//        reviewHolder.add(new ReviewCardComponent(review));
//        components.forEach(reviewHolder::add);
    }
}