package help.sausage.ui.component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import help.sausage.client.ReviewClient;
import help.sausage.dto.ReviewDto;
import help.sausage.dto.ReviewUpdateDto;
import help.sausage.ui.data.Review;
import help.sausage.utils.ApplicationContextProvider;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class EditReviewForm extends ReviewForm<ReviewUpdateDto> {

    protected final ReviewClient reviewClient;
    private Review review;
    private Dialog dialog;

    public EditReviewForm(Review review, Dialog dialog) {
        super();
        this.reviewClient = ApplicationContextProvider.getCtx().getBean(ReviewClient.class);
        this.review = review;
        this.dialog = dialog;
        reviewArea.setValue(review.text());
        datePicker.setValue(review.dateReview());
        review.crims().forEach(c -> crimBoxHolder.add(createCrimBox(c.name(), crimBoxHolder)));
        starsVoteComponent.setAmount(review.stars());
        int stars = review.stars();
        if (stars == 0) {
            starsVoteComponent.reset();
        }
        starsVoteComponent.setIcons(review.stars() - 1);
    }

    @Override
    public ReviewUpdateDto getData() {
        List<String> crimNames = crimBoxHolder.getChildren()
                .map(el -> ((KnownCrimBtnComponent) el).getCrimName()).toList();
        return new ReviewUpdateDto(
                crimNames,
                datePicker.getValue(),
                starsVoteComponent.getAmount(),
                reviewArea.getValue());
    }

    @Override
    void submitNewReview(ReviewUpdateDto data) {
        ResponseEntity<ReviewDto> response = reviewClient.updateReview(review.reviewId(), data);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            Notification.show("error creating new review: %s".formatted(data.toString()));
            log.error(response.toString());
        } else {
            reviewCreatedListeners.forEach(l -> l.onNewReview(response.getBody()));
            if (dialog != null) {
                dialog.close();
            }
            UI.getCurrent().getPage().reload();
        }
    }

}
