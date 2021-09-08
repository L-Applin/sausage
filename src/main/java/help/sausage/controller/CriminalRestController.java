package help.sausage.controller;

import static help.sausage.controller.CriminalController.BASE_URL;

import help.sausage.dto.CrimInfoDto;
import help.sausage.dto.ReviewDto;
import help.sausage.service.CriminalService;
import help.sausage.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class CriminalRestController implements CriminalController {

    private final ReviewService reviewService;
    private final CriminalService criminalService;

    @GetMapping(CRIM_REVIEWS_URL)
    public ResponseEntity<List<ReviewDto>> getReviewForCrim(
            @PathVariable String username,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "dateCreated", required = false) String sortBy,
            @RequestParam(defaultValue = "desc", required = false) String dir) {
        return ResponseEntity.ok(reviewService.getReviewByCrimUsername(username, page, size, sortBy, dir));
    }

    @GetMapping(CRIM_INFO_URL)
    public ResponseEntity<CrimInfoDto> getCrimInfo(@PathVariable("username") String username) {
        return ResponseEntity.ok(criminalService.getCrimInfo(username));
    }

}
