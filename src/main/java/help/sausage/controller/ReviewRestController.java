package help.sausage.controller;

import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import help.sausage.service.ReviewService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
@Slf4j
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
            @RequestParam(defaultValue = "reviewDate", required = false) String sortBy,
            @RequestParam(defaultValue = "desc", required = false) String dir) {
        Page<ReviewDto> reviews = reviewService.getReviewsPaginated(page, size, sortBy, dir);
        ResponseEntity<List<ReviewDto>> res = ResponseEntity.ok(reviews.toList());
        log.info(res.toString());
        return res;
    }

    @GetMapping(GET_REVIEW_BY_USERNAME_URL)
    public ResponseEntity<List<ReviewDto>> getReviewByUsername(@PathVariable String username) {
        List<ReviewDto> reviews = reviewService.getReviewsByAuthorUsername(username);
        return ResponseEntity.ok(reviews);
    }


}
