package help.sausage.ui;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import help.sausage.client.ReviewClient;
import help.sausage.dto.ReviewDto;
import help.sausage.ui.component.ReviewComponent;
import help.sausage.ui.component.ReviewFormComponent;
import help.sausage.ui.data.Review;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@Route
public class MainView extends VerticalLayout {
    private static final long serialVersionUID = 42L;

    public static final String LOGGED_USERNAME_ATTRIBUTE_KEY = "username";

    private final ReviewClient reviewClient;

    public MainView(@Autowired ReviewClient reviewClient) {
        this.reviewClient = reviewClient;

        VaadinSession session = VaadinSession.getCurrent();
        Object user = session.getAttribute(LOGGED_USERNAME_ATTRIBUTE_KEY);
        if (user == null) {
            Notification.show("No user logged in");
        }

        VerticalLayout centerColumn = new VerticalLayout();
        centerColumn.setWidth("640px");
        centerColumn.getStyle().set("border-left",  "1px solid gray");
        centerColumn.getStyle().set("border-right", "1px solid gray");
        centerColumn.getStyle().set("padding", "0px");

        HorizontalLayout topBar = topBar();

        ReviewFormComponent buildForm = new ReviewFormComponent();
        VerticalLayout reviewHolder = new VerticalLayout();
        reviewHolder.setWidth("100%");
        reviewHolder.setPadding(false);

        ResponseEntity<List<ReviewDto>> resp = reviewClient.getAllReviewsPaginated();
        if (!resp.getStatusCode().is2xxSuccessful()) {
            Notification.show("Error while retrieving reviews: " + resp.toString());
        } else {
            List<ReviewDto> reviews = resp.getBody();
            if (reviews != null && !reviews.isEmpty()) {
                reviews.forEach(
                        review -> reviewHolder.add(new ReviewComponent(Review.fromDto(review))));
            }
        }

        centerColumn.add(topBar, buildForm, reviewHolder);

        HorizontalLayout wrapper = new HorizontalLayout(new Span("TMP DEMO 1"), centerColumn, new Span("TMP DEMO 2"));
        setHorizontalComponentAlignment(Alignment.CENTER, wrapper);
        add(wrapper);
    }

    private HorizontalLayout topBar() {
        HorizontalLayout topBar = new HorizontalLayout();
        H2 title = new H2("Home");
        title.getStyle().set("margin", "0px");
        title.getStyle().set("padding", "0px");
        title.getStyle().set("padding-left", "22px");
        topBar.add(title);
        topBar.getStyle().set("border-bottom", "1px solid gray");
        topBar.setWidth("100%");
        return topBar;
    }

}