package help.sausage.controller;

import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import help.sausage.dto.ReviewUpdateDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface ReviewController {
    String BASE_URL = "/api/review";

    ResponseEntity<ReviewDto> createNewReview(NewReviewDto reviewDto);

    ResponseEntity<List<ReviewDto>> getAllReviewsPaginated(int page, int size, String sortBy, String dir);

    String GET_REVIEW_BY_ID_URL = "/{reviewId}";
    ResponseEntity<ReviewDto> getReviewById(UUID reviewId);

    String PATCH_SEND_LIKE_URL = "/{reviewId}/like";
    ResponseEntity<Long> sendLike(UUID reviewId);

    String HAS_LIKED_REVIEWD_URL = "/user/{reviewId}/like";
    ResponseEntity<Boolean> hasUserLiked(UUID ReviewId);


    String PATCH_REVIEW_URL = "/{reviewId}";
    ResponseEntity<ReviewDto> updateReview(UUID reviewId, ReviewUpdateDto review);

    String GET_TOTAL_REVIEW_COUNT_URL = "/count";
    ResponseEntity<Long> getTotalReviewCount();

    String GET_SEARCH = "/search";
    ResponseEntity<List<ReviewDto>> searchReview(
        Optional<String> fullText,
        List<String> searchTerms,
        Optional<LocalDate> startDate,
        Optional<LocalDate> endDate,
        int page,
        int size,
        String sortBy,
        String dir);
}
