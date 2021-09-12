package help.sausage.ui.component;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import help.sausage.client.ReviewClient;
import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import help.sausage.ui.data.SessionUser;
import help.sausage.utils.ApplicationContextProvider;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@CssImport(value="./styles/review-text-field.css", themeFor="vaadin-text-area")
@Slf4j
public class ReviewFormComponent extends ReviewForm<NewReviewDto> {

    protected final ReviewClient reviewClient;

    public ReviewFormComponent() {
        this.reviewClient = ApplicationContextProvider.getCtx().getBean(ReviewClient.class);
    }

    @Override
    public NewReviewDto getData() {
        SessionUser user = VaadinSession.getCurrent().getAttribute(SessionUser.class);
        List<String> crimNames = crimBoxHolder.getChildren()
                .map(el -> ((KnownCrimBtnComponent) el).getCrimName()).toList();
        return new NewReviewDto(
                user.uuid(),
                crimNames,
                datePicker.getValue(),
                starsVoteComponent.getAmount(),
                reviewArea.getValue(),
                imageLink.getValue());
    }

    @Override
    void submitNewReview(NewReviewDto data) {
        ResponseEntity<ReviewDto> response = reviewClient.createNewReview(data);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            Notification.show("error creating new review: %s".formatted(data.toString()));
            log.error(response.toString());
        } else {
            reviewCreatedListeners.forEach(l -> l.onNewReview(response.getBody()));
        }
    }
}
