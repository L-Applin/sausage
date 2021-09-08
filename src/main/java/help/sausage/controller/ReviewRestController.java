package help.sausage.controller;

import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import help.sausage.dto.ReviewUpdateDto;
import help.sausage.service.ReviewService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ReviewController.BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class ReviewRestController implements ReviewController {

    private final ReviewService reviewService;

    @Override
    @PostMapping
    public ResponseEntity<ReviewDto> createNewReview(@RequestBody NewReviewDto reviewDto) {
        ReviewDto created = reviewService.createNewReview(reviewDto);
        return ResponseEntity.ok(created);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviewsPaginated(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "dateCreated", required = false) String sortBy,
            @RequestParam(defaultValue = "desc", required = false) String dir) {
        Page<ReviewDto> reviews = reviewService.getReviewsPaginated(page, size, sortBy, dir);
        ResponseEntity<List<ReviewDto>> res = ResponseEntity.ok(reviews.toList());
        return res;
    }

    @Override
    @GetMapping(GET_REVIEW_BY_USERNAME_URL)
    public ResponseEntity<List<ReviewDto>> getReviewByUsername(@PathVariable String username) {
        List<ReviewDto> reviews = reviewService.getReviewsByAuthorUsername(username);
        return ResponseEntity.ok(reviews);
    }

    @Override
    @PatchMapping(SEND_LIKE_URL)
    public ResponseEntity<Long> sendLike(@PathVariable("reviewId") UUID reviewId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            String msg = "Liked review '%s' by unauthenticated user".formatted(reviewId.toString());
            log.warn(msg);
            throw new UsernameNotFoundException(msg);
        }
        long totalLikes = reviewService.addLike(reviewId, auth.getName());
        return ResponseEntity.ok().body(totalLikes);
    }

    @GetMapping(HAS_LIKED_REVIEWD_URL)
    public ResponseEntity<Boolean> hasUserLiked(@PathVariable("reviewId") UUID reviewId) {
        return ResponseEntity.ok(reviewService.hasLiked(getLoggedUserName(), reviewId));
    }


    @Override
    @PatchMapping(PATCH_REVIEW_URL)
    public ResponseEntity<ReviewDto> updateReview(@PathVariable("reviewId") UUID reviewId,
            @RequestBody ReviewUpdateDto review) {
        ReviewDto updatedReview = reviewService.updateReview(reviewId, review);
        return ResponseEntity.ok(updatedReview);
    }

    private String getLoggedUserName() {
        return getLoggedUserName(
        "Acces to an end-point that requires authentification without login");
    }

    private String getLoggedUserName(String errorMsg) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            log.warn(errorMsg);
            throw new UsernameNotFoundException(errorMsg);
        }
        return auth.getName();
    }

}
