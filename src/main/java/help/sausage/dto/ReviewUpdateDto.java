package help.sausage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateDto {
    @JsonProperty
    private List<String> crims;
    @JsonProperty
    private LocalDate date;
    @JsonProperty
    private Integer stars;
    @JsonProperty
    private String text;
}
