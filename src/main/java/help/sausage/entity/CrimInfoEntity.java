package help.sausage.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "sausage", name="crim_info")
public class CrimInfoEntity {

    @Id
    private String username;
    private String icon;
    private long total;
    private double score;
    @Column(name = "first_review")
    private LocalDate firstReview;
    @Column(name = "last_review")
    private LocalDate lastReview;

}
