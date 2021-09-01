package help.sausage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", schema = "sausage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    private String username;
    @Column
    private String password;
    @Column
    private boolean enabled;
}
