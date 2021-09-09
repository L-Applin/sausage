package help.sausage.service;

import static help.sausage.entity.UnknownCrimEntity.newUnknownCrim;
import static help.sausage.service.ReviewMapper.toDto;

import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import help.sausage.dto.ReviewUpdateDto;
import help.sausage.entity.AppUserEntity;
import help.sausage.entity.ReviewEntity;
import help.sausage.entity.ReviewLikeEntity;
import help.sausage.entity.ReviewLikeId;
import help.sausage.entity.UnknownCrimEntity;
import help.sausage.entity.UnknownCrimId;
import help.sausage.repository.AppUserRepository;
import help.sausage.repository.ReviewLikeRepository;
import help.sausage.repository.ReviewRepository;
import help.sausage.repository.UnkownCrimsRepository;
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
                null,
                reviewDto.getStars(),
                reviewDto.getText());
        ReviewEntity saved = reviewRepository.save(toSave);
        unkownCrims.forEach(str -> unkownCrimsRepository.save(
                newUnknownCrim(str, saved.getReviewId())));
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

    public List<ReviewDto> getReviewByCrimUsername(String crimUsername, int page, int size, String sortBy, String direction) {
        Direction dir = Direction.fromOptionalString(direction).orElse(Direction.DESC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        return reviewRepository.findAllByCrimsUsername(crimUsername, pageable)
                .stream().map(this::withTotalLikes).toList();
    }

    @Transactional
    public ReviewDto updateReview(UUID reviewId, ReviewUpdateDto review) {
        int totalLikes = (int) reviewRepository.countNumberOfLikes(reviewId.toString());
        List<AppUserEntity> knownCrims = new ArrayList<>();
        boolean requiresCrimUpdate = review.getCrims() != null;
        if (requiresCrimUpdate) {
            unkownCrimsRepository.deleteAllByIdReviewId(reviewId);
        }
        ReviewEntity previous = reviewRepository.findById(reviewId).orElseThrow(
                () -> new NoSuchElementException("Cannot find review with id '%s'".formatted(reviewId)));
        if (requiresCrimUpdate) {
            List<String> unkownCrims = new ArrayList<>();
            review.getCrims().forEach(username -> appUserRepository.findByUsername(username)
                    .ifPresentOrElse(knownCrims::add, () -> unkownCrims.add(username)));

            List<UnknownCrimEntity> unknownCrimEntities = unkownCrims.stream()
                    .map(c -> unkownCrimsRepository.findById(new UnknownCrimId(c, reviewId))
                        .orElseGet(() -> unkownCrimsRepository.save(newUnknownCrim(c, reviewId))))
                    .toList();
            previous.setCrims(knownCrims);
            previous.setUnknownCrims(unknownCrimEntities);
        }
        if (review.getStars() != null) {
            previous.setStars(review.getStars());
        }

        if (review.getText() != null) {
            previous.setText(review.getText());
        }

        if (review.getDate() != null) {
            previous.setReviewDate(review.getDate());
        }
        previous.setUpdated(LocalDateTime.now());
        return withTotalLikes(previous, totalLikes);
    }

    public long addLike(UUID reviewId, String username) {
        UUID userId = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Cannot find user with username '%s'"
                        .formatted(username)))
                .getUserId();
        ReviewLikeEntity like = new ReviewLikeEntity(new ReviewLikeId(reviewId, userId));
        reviewLikeRepository.save(like);
        return reviewRepository.countNumberOfLikes(reviewId.toString());
    }

    public boolean hasLiked(String username, UUID reviewId) {
        return appUserRepository
                .findByUsername(username)
                .flatMap(u -> reviewLikeRepository.findById(new ReviewLikeId(reviewId, u.getUserId())))
                .isPresent();
    }

    private ReviewDto withTotalLikes(ReviewEntity reviewEntity) {
        final String reviewId = reviewEntity.getReviewId().toString();
        long totalLikes = reviewRepository.countNumberOfLikes(reviewId);
        return toDto(reviewEntity, totalLikes, 0); //todo @Comments
    }

    private ReviewDto withTotalLikes(ReviewEntity reviewEntity, int totalLikes) {
        return toDto(reviewEntity, totalLikes, 0); //todo @Comments
    }

    public ReviewDto getReviewById(UUID reviewId) {
        return withTotalLikes(reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Cannot find review with id '%s'"
                        .formatted(reviewId.toString()))));
    }

    public long countTotalReview() {
        return reviewRepository.count();
    }
}
