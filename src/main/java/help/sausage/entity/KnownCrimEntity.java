package help.sausage.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "crims_involved")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KnownCrimEntity {

    @EmbeddedId
    private KnownCrimEntityId id;

}
