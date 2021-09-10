package help.sausage.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import help.sausage.client.ReviewClient;
import help.sausage.client.UserClient;
import help.sausage.dto.ReviewDto;
import help.sausage.dto.UserDto;
import help.sausage.ui.component.LeftColumnComponent;
import help.sausage.ui.component.ReviewFormComponent;
import help.sausage.ui.component.ReviewHolderComponent;
import help.sausage.ui.component.RightColumnComponent;
import help.sausage.ui.data.Review;
import help.sausage.ui.data.SessionUser;
import help.sausage.utils.ApplicationContextProvider;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;

/*
<div>Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
 */
@Route
@CssImport("./styles/main-view.css")
@PageTitle("Home | Sausage")
public class MainView extends VerticalLayout implements ReviewFormComponent.ReviewCreatedListener {

    private final ReviewClient reviewClient;
    private ReviewHolderComponent reviewHolder;
    private final UserClient userClient;
    private SessionUser user;

    public MainView() {
        this.userClient = ApplicationContextProvider.getCtx().getBean(UserClient.class);
        this.reviewClient = ApplicationContextProvider.getCtx().getBean(ReviewClient.class);

        setClassName("main-vew");

        user = VaadinSession.getCurrent().getAttribute(SessionUser.class);
        if (user == null) {
            String username = (String) VaadinSession.getCurrent().getAttribute("username");
            if (username != null) {
                ResponseEntity<UserDto> fetchedUserResponse = userClient
                        .getUserByUsername(username);
                if (!fetchedUserResponse.getStatusCode().is2xxSuccessful()
                        || fetchedUserResponse.getBody() == null) {
                    // error while fetching full user from backend
                    Notification
                            .show("There was an error while trying to retreive user information.");
                } else {
                    UserDto fetchedUser = fetchedUserResponse.getBody();
                    final SessionUser sessionUser = new SessionUser(fetchedUser.username(),
                            fetchedUser.id(),
                            fetchedUser.icon());
                    VaadinSession.getCurrent().setAttribute(SessionUser.class,
                            sessionUser);
                    user = sessionUser;
                }
            } else {
                UI.getCurrent().navigate(LoginView.class);
                return;
            }
        }

        VerticalLayout centerColumn = new VerticalLayout();
        centerColumn.setClassName("main-view-center");
        centerColumn.getStyle().clear();

        ReviewFormComponent reviewForm = new ReviewFormComponent();
        reviewForm.addOnReviewCreatedListener(this);
        reviewForm.setClassName("main-review-form");

        this.reviewHolder = new ReviewHolderComponent(this::queryReview, true);
        centerColumn.add(reviewForm, reviewHolder);
        VerticalLayout left = new LeftColumnComponent();
        left.getStyle().clear();
        VerticalLayout right = new RightColumnComponent();
        right.getStyle().clear();
        left.setClassName("main-left-column");
        right.setClassName("main-right-column");

        HorizontalLayout wrapper = new HorizontalLayout(left, centerColumn, right);
        setHorizontalComponentAlignment(Alignment.CENTER, wrapper);
        wrapper.setClassName("main-view-horizontal-wrapper");
        add(wrapper);
    }

    @Override
    public void onNewReview(ReviewDto reviewDto) {
        UI.getCurrent().getPage().reload();
    }

    public Stream<Review> queryReview(Query<Review, Void> query) {
        return reviewClient.getAllReviewsPaginated(query.getPage(), query.getLimit())
                .getBody().stream().map(Review::fromDto); // todo @Error how to handle backend error here ???
    }

}