package help.sausage.controller;

import help.sausage.dto.ReviewDto;
import help.sausage.service.CriminalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/criminal")
@RequiredArgsConstructor
public class CriminalRestController {

    private final CriminalService criminalService;

    @GetMapping("/review/{username}")
    public ResponseEntity<List<ReviewDto>> getReviewForCrim(@PathVariable String username) {
        return ResponseEntity.ok(criminalService.getReviewBuCrimUsername(username));
    }
}
