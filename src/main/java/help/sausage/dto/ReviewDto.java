package help.sausage.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    @NotNull
    private UUID reviewId;
    @NotNull
    private AuthorDto author;
    @NotNull
    private List<String> knownCrims;
    @NotNull
    private List<String> unknownCrims;
    @NotNull
    private LocalDateTime dateCreated;
    @NotNull
    private LocalDate dateReview;
    @NotNull
    private int stars;
    @NotNull
    private long totalLikes;
    @NotNull
    private long totalComments;
    @NotNull
    private String text;

    private String imgUrl;
}
