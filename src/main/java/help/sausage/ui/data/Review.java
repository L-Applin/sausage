package help.sausage.ui.data;

import help.sausage.dto.ReviewDto;
import help.sausage.dto.ReviewUpdateDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record Review (
    UUID reviewId,
    Author author,
    List<Crim> crims,
    LocalDateTime dateCreated,
    LocalDate dateReview,
    int stars,
    String text,
    long likes,
    long comments
    ) {

    public static Review fromDto(ReviewDto review) {
        return new Review(
            review.getReviewId(),
            new Author(review.getAuthor().getName(), review.getAuthor().getIcon()),
            Stream.concat(
                review.getKnownCrims().stream().map(c -> new Crim(c, true)),
                review.getUnknownCrims().stream().map(c -> new Crim(c, false)))
                    .toList(),
            review.getDateCreated(),
            review.getDateReview(),
            review.getStars(),
            review.getText(),
            review.getTotalLikes(),
            review.getTotalComments()
        );

    }

    public ReviewUpdateDto toReviewUpdateDto(boolean updateCrims) {
        final List<String> knownCrims = crims.stream()
                .filter(Crim::isKnown)
                .map(Crim::name)
                .toList();
        final List<String> unknownCrim = crims.stream()
                .filter(Predicate.not(Crim::isKnown))
                .map(Crim::name)
                .toList();
        return new ReviewUpdateDto(
                knownCrims,
                unknownCrim,
                dateReview(),
                stars(),
                text(),
                updateCrims);
    }

    public static record Crim(String name, boolean isKnown) {}
    public static record Author(String name, String icon) {}
}


