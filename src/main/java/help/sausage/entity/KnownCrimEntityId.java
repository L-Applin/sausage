package help.sausage.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KnownCrimEntityId implements Serializable {

    @Column(name = "review_id")
    @Type(type = "uuid-char")
    private UUID reviewId;

    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID userId;
}
