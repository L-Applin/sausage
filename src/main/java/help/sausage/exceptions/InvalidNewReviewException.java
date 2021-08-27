package help.sausage.exceptions;

import help.sausage.dto.NewReviewDto;
import lombok.Getter;

public class InvalidNewReviewException extends RuntimeException {

    @Getter
    private NewReviewDto newReview;

    public InvalidNewReviewException(NewReviewDto newReview, String message) {
        super(message);
        this.newReview = newReview;
    }
}
