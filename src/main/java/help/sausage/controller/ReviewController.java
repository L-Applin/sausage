package help.sausage.controller;

import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReviewController {

    String BASE_URL = "/v1/review";

    ResponseEntity<ReviewDto> createNewReview(@RequestBody @Valid NewReviewDto reviewDto);

    ResponseEntity<List<ReviewDto>> getAllReviewsPaginated(int page, int size, String sortBy, String dir);

    String GET_REVIEW_BY_USERNAME_URL = "/{username}";
    ResponseEntity<List<ReviewDto>> getReviewByUsername(@PathVariable String username);
}
