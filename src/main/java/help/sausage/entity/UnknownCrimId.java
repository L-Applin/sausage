package help.sausage.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnknownCrimId implements Serializable {

    @Column(name = "crim_name", table = "unknown_crims_involved")
    private String crimName;

    @Column(name = "review_id", table = "unknown_crims_involved")
    @Type(type = "uuid-char")
    private UUID reviewId;
}
