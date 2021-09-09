package help.sausage.ui;

import static help.sausage.ui.data.Review.fromDto;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import help.sausage.client.ReviewClient;
import help.sausage.client.UserClient;
import help.sausage.dto.ReviewDto;
import help.sausage.dto.UserDto;
import help.sausage.ui.component.LeftColumnComponent;
import help.sausage.ui.component.ReviewCardComponent;
import help.sausage.ui.component.ReviewFormComponent;
import help.sausage.ui.component.RightColumnComponent;
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
public class MainView extends VerticalLayout implements ReviewFormComponent.ReviewCreatedListener {
    private static final long serialVersionUID = 42L;

    private final VerticalLayout reviewHolder = new VerticalLayout();
    private final ReviewClient reviewClient;
    private final UserClient userClient;
    private SessionUser user;

    public MainView() {
        this.reviewClient = ApplicationContextProvider.getCtx().getBean(ReviewClient.class);
        this.userClient = ApplicationContextProvider.getCtx().getBean(UserClient.class);
        setClassName("main-vew");

        user = VaadinSession.getCurrent().getAttribute(SessionUser.class);
        if (user == null) {
            String username = (String) VaadinSession.getCurrent().getAttribute("username");
            ResponseEntity<UserDto> fetchedUserResponse = userClient.getUserByUsername(username);
            if (!fetchedUserResponse.getStatusCode().is2xxSuccessful()
                    || fetchedUserResponse.getBody() == null) {
                // error while fetching full user from backend
                Notification.show("There was an error while trying to retreive user information.");
            } else {
                UserDto fetchedUser = fetchedUserResponse.getBody();
                final SessionUser sessionUser = new SessionUser(fetchedUser.username(), fetchedUser.id(),
                        fetchedUser.icon());
                VaadinSession.getCurrent().setAttribute(SessionUser.class,
                        sessionUser);
                user = sessionUser;
            }
        }

        VerticalLayout centerColumn = new VerticalLayout();
        centerColumn.setClassName("main-view-center");

        ReviewFormComponent reviewForm = new ReviewFormComponent();
        reviewForm.addOnReviewCreatedListener(this);
        reviewForm.setClassName("main-review-form");
        reviewHolder.setPadding(false);
        reviewHolder.setId("main-review-holder");

        ResponseEntity<List<ReviewDto>> resp = reviewClient.getAllReviewsPaginated();
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            Notification.show("Error while retrieving reviews: " + resp.toString());
        } else {
            resp.getBody().forEach(
                    review -> reviewHolder.add(new ReviewCardComponent(fromDto(review))));
        }

        centerColumn.add(reviewForm, reviewHolder);
        VerticalLayout left = new LeftColumnComponent();
        VerticalLayout right = new RightColumnComponent();
        left.setClassName("main-left-column");
        right.setClassName("main-right-column");

        HorizontalLayout wrapper = new HorizontalLayout(left, centerColumn, right);
        wrapper.expand(centerColumn);
        setHorizontalComponentAlignment(Alignment.CENTER, wrapper);
        wrapper.setClassName("main-view-horizontal-wrapper");
        add(wrapper);
    }

    @Override
    public void onNewReview(ReviewDto reviewDto) {
        UI.getCurrent().getPage().reload();
    }
}