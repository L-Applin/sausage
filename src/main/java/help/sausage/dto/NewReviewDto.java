package help.sausage.dto;

import help.sausage.validation.ValidNewReview;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidNewReview
public class NewReviewDto {

    private UUID authorId;
    private List<String> crims;
    private LocalDate date;
    private int stars;
    private String text;

}
