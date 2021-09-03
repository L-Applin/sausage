package help.sausage.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "app_user", schema = "sausage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "icon")
    private String icon;

    @Column(name = "date_joined")
    private LocalDateTime dateJoined;

}
