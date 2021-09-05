package help.sausage.service;

import static help.sausage.service.ReviewMapper.toDto;

import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import help.sausage.entity.ReviewEntity;
import help.sausage.entity.ReviewLikeEntity;
import help.sausage.entity.ReviewLikeId;
import help.sausage.entity.UnknownCrimEntity;
import help.sausage.entity.AppUserEntity;
import help.sausage.repository.ReviewLikeRepository;
import help.sausage.repository.ReviewRepository;
import help.sausage.repository.UnkownCrimsRepository;
import help.sausage.repository.AppUserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    private final AppUserRepository appUserRepository;
    private final UnkownCrimsRepository unkownCrimsRepository;

    @Transactional
    public ReviewDto createNewReview(NewReviewDto reviewDto) {
        List<AppUserEntity> knownCrims = new ArrayList<>();
        List<String> unkownCrims = new ArrayList<>();
        reviewDto.getCrims().forEach(username ->
                appUserRepository.findByUsername(username).ifPresentOrElse(knownCrims::add, () -> unkownCrims.add(username)));
        AppUserEntity author = appUserRepository.findById(reviewDto.getAuthorId()).orElseThrow(() ->
                new NoSuchElementException("No author found with id '%s'".formatted(reviewDto.getAuthorId())));
        // @Error - better reporting
        ReviewEntity toSave = new ReviewEntity(
                null,
                author ,
                knownCrims,
                Collections.emptyList(),
                LocalDateTime.now(),
                reviewDto.getDate(),
                reviewDto.getStars(),
                reviewDto.getText());
        ReviewEntity saved = reviewRepository.save(toSave);
        unkownCrims.forEach(str -> unkownCrimsRepository.save(
                UnknownCrimEntity.newUnknownCrim(str, saved.getReviewId())));
        return withTotalLikes(saved);
    }

    public List<ReviewDto> getReviewsByAuthorUsername(String username) {
        List<ReviewEntity> reviews = reviewRepository.findAllByAuthorUsername(username, Sort.by("reviewDate"));
        return reviews.stream().map(this::withTotalLikes)
                .toList();
    }

    public Page<ReviewDto> getReviewsPaginated(int page, int size, String sortBy, String direction) {
        Direction dir = Direction.fromOptionalString(direction).orElse(Direction.DESC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        return reviewRepository.findAll(pageable).map(this::withTotalLikes);
    }

    public List<ReviewDto> getReviewByCrimUsername(String crimUsername) {
        return reviewRepository.findAllCrimReviewByCrimName(crimUsername)
                .stream().map(this::withTotalLikes).toList();
    }

    public long addLike(UUID reviewId, String username) {
        UUID userId = appUserRepository.findByUsername(username)
                .orElseThrow().getUserId();
        ReviewLikeEntity like = new ReviewLikeEntity(new ReviewLikeId(reviewId, userId));
        reviewLikeRepository.save(like);
        return reviewRepository.countNumberOfLikes(reviewId.toString());
    }

    private ReviewDto withTotalLikes(ReviewEntity reviewDto) {
        long totalLikes = reviewRepository.countNumberOfLikes(reviewDto.getReviewId().toString());
        return toDto(reviewDto, totalLikes, 0); //fixme @Comments
    }

    public boolean hasLiked(String username, UUID reviewId) {
        return appUserRepository
                .findByUsername(username)
                .flatMap(u -> reviewLikeRepository.findById(new ReviewLikeId(reviewId, u.getUserId())))
                .isPresent();
    }
}
