package help.sausage.dto;

import help.sausage.entity.UserEntity;
import help.sausage.validation.ValidNewReview;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
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
    private Date date;
    private int stars;
    private String text;

}
