package help.sausage.ui.data;

import help.sausage.dto.ReviewDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;

public record Review (
    Author author,
    List<Crim> crims,
    LocalDate dateCreated,
    LocalDate dateReview,
    int stars,
    String text) {

    public static Review fromDto(ReviewDto review) {
        return new Review(
            new Author(review.getAuthor().getName(), review.getAuthor().getIcon()),
            Stream.concat(
                review.getKnownCrims().stream().map(c -> new Crim(c, true)),
                review.getUnknownCrims().stream().map(c -> new Crim(c, false)))
                    .toList(),
            review.getDateCreated(),
            review.getDateReview(),
            review.getStars(),
            review.getText()
        );
    }

    public static record Crim(String name, boolean isKnown) {}
    public static record Author(String name, String icon) {}
}


