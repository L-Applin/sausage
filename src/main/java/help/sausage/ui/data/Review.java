package help.sausage.ui.data;

import help.sausage.dto.ReviewDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
    ) implements Comparable<Review>{

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
            review.getTotalComments());
    }

    public static record Crim(String name, boolean isKnown) {}
    public static record Author(String name, String icon) {}

    @Override
    public int compareTo(Review o) {
        return 0;
    }
}
