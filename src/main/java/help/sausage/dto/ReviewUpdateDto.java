package help.sausage.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateDto {
    private UUID authorId;
    private List<String> knownCrims;
    private List<String> unknownCrim;
    private LocalDate date;
    private int stars;
    private String text;
}
