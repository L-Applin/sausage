package help.sausage.controller;

import help.sausage.dto.CrimInfoDto;
import help.sausage.dto.ReviewDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface CriminalController {
    String BASE_URL = "/api/criminal";

    String CRIM_REVIEWS_URL = "/reviews/{username}";
    ResponseEntity<List<ReviewDto>> getReviewForCrim(String username, int page, int size, String sortBy, String dir);

    String CRIM_INFO_URL = "/{username}";
    ResponseEntity<CrimInfoDto> getCrimInfo(String username);
}
