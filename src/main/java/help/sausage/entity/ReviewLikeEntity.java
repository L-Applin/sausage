package help.sausage.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Table(name = "review_likes")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewLikeEntity {

    @EmbeddedId
    private ReviewLikeId id;

}
