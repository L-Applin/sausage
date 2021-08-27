package help.sausage.entity;

import java.util.UUID;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "unknown_crims_involved")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnknownCrimEntity {

    @EmbeddedId
    private UnknownCrimId id;

    @ManyToOne
    @JoinColumn(name = "review_id", insertable = false, updatable = false)
    private ReviewEntity review;

    public static UnknownCrimEntity newUnknownCrim(String crim, UUID reviewId) {
        return new UnknownCrimEntity(new UnknownCrimId(crim, reviewId), null);
    }
}
