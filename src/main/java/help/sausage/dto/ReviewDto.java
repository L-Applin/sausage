package help.sausage.dto;

import java.time.LocalDate;
import java.util.Date;
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
    private String author;
    @NotNull
    private List<String> knownCrims;
    @NotNull
    private List<String> unknownCrims;
    @NotNull
    private LocalDate date;
    @NotNull
    private int stars;
    @NotNull
    private String text;
}
