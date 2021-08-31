package help.sausage.controller;

import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface CriminalController {
    ResponseEntity<ReviewDto> createNewReview(@RequestBody @Valid NewReviewDto reviewDto);
    ResponseEntity<List<ReviewDto>> getAllReviewsPaginated(int page, int size, String sortBy, String dir);

    ResponseEntity<List<ReviewDto>> getReviewByUsername(@PathVariable String username);

}
