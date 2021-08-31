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
    String authorName,
    List<Crim> crims,
    LocalDate date,
    int stars,
    String text) {

    public static Review fromDto(ReviewDto review) {
        return new Review(
            review.getAuthor(),
            Stream.concat(
                review.getKnownCrims().stream().map(c -> new Crim(c, true)),
                review.getUnknownCrims().stream().map(c -> new Crim(c, false)))
                    .toList(),
            review.getDate(),
            review.getStars(),
            review.getText()
        );
    }

    public static record Crim(String name, boolean isKnown) {}
}


