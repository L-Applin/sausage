package help.sausage.controller;

import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import help.sausage.service.ReviewService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ReviewController.BASE_URL)
@RequiredArgsConstructor
public class ReviewRestController implements ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createNewReview(@RequestBody @Valid NewReviewDto reviewDto) {
        ReviewDto created = reviewService.createNewReview(reviewDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviewsPaginated(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "date", required = false) String sortBy,
            @RequestParam(defaultValue = "desc", required = false) String dir) {
        Page<ReviewDto> reviews = reviewService.getReviewsPaginated(page, size, sortBy, dir);
        return ResponseEntity.ok(reviews.toList());
    }

    @GetMapping(GET_REVIEW_BY_USERNAME_URL)
    public ResponseEntity<List<ReviewDto>> getReviewByUsername(@PathVariable String username) {
        List<ReviewDto> reviews = reviewService.getReviewsByAuthorUsername(username);
        return ResponseEntity.ok(reviews);
    }


}
