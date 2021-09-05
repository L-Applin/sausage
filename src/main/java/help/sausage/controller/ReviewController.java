package help.sausage.controller;

import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReviewController {
    String BASE_URL = "/api/review";

    ResponseEntity<ReviewDto> createNewReview(NewReviewDto reviewDto);

    ResponseEntity<List<ReviewDto>> getAllReviewsPaginated(int page, int size, String sortBy, String dir);

    String GET_REVIEW_BY_USERNAME_URL = "/{username}";
    ResponseEntity<List<ReviewDto>> getReviewByUsername(String username);

    String SEND_LIKE_URL = "/{reviewId}/like";
    ResponseEntity<Long> sendLike(UUID reviewId);

    String HAS_LIKED_REVIEWD_URL = "/user/{reviewId}/like";
    ResponseEntity<Boolean> hasUserLiked(UUID ReviewId);
}
