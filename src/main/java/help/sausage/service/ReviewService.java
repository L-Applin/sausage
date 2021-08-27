package help.sausage.service;

import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import help.sausage.entity.ReviewEntity;
import help.sausage.entity.UnknownCrimEntity;
import help.sausage.entity.UserEntity;
import help.sausage.repository.ReviewRepository;
import help.sausage.repository.UnkownCrimsRepository;
import help.sausage.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewService {

    public final ReviewRepository reviewRepository;

    public final UserRepository userRepository;
    public final UnkownCrimsRepository unkownCrimsRepository;

    @Transactional
    public ReviewDto createNewReview(NewReviewDto reviewDto) {
        List<UserEntity> knownCrims = new ArrayList<>();
        List<String> unkownCrims = new ArrayList<>();
        reviewDto.getCrims().forEach(username ->
                userRepository.findByUsername(username).ifPresentOrElse(knownCrims::add, () -> unkownCrims.add(username)));
        UserEntity author = userRepository.findById(reviewDto.getAuthorId()).orElseThrow(); // @Error - better reporting
        ReviewEntity toSave = new ReviewEntity(null, author , knownCrims, Collections.emptyList(), reviewDto.getDate(), reviewDto.getStars(), reviewDto.getText());
        ReviewEntity saved = reviewRepository.save(toSave);
        unkownCrims.forEach(str -> unkownCrimsRepository.save(
                UnknownCrimEntity.newUnknownCrim(str, saved.getReviewId())));
        return new ReviewDto(
            saved.getReviewId(),
            author.getUsername(),
            knownCrims.stream().map(UserEntity::getUsername).toList(),
            unkownCrims,
            reviewDto.getDate(),
            reviewDto.getStars(),
            reviewDto.getText()
        );
    }

    public List<ReviewDto> getReviewsByAuthorUsername(String username) {
        List<ReviewEntity> reviews = reviewRepository.findAllByAuthorIdUsername(username, Sort.by("date"));
        return reviews.stream().map(ReviewMapper::toDto).toList();
    }

    public Page<ReviewDto> getReviewsPaginated(int page, int size, String sortBy, String direction) {
        Direction dir = Direction.fromOptionalString(direction).orElse(Direction.DESC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        return reviewRepository.findAll(pageable).map(ReviewMapper::toDto);
    }

}
