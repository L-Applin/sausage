package help.sausage.service;

import help.sausage.dto.ReviewDto;
import help.sausage.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriminalService {

    private final ReviewRepository reviewRepository;

    public List<ReviewDto> getReviewBuCrimUsername(String crimUsername) {
        return reviewRepository.findAllCrimReviewByCrimName(crimUsername)
                .stream().map(ReviewMapper::toDto).toList();
    }
}
