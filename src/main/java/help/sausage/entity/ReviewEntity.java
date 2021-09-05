package help.sausage.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "reviews", schema = "sausage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID reviewId;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private AppUserEntity author;

    @ManyToMany
    @JoinTable(
            name = "crims_involved",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<AppUserEntity> crims;

    @OneToMany(mappedBy = "review")
    private List<UnknownCrimEntity> unknownCrims;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_review")
    private LocalDate reviewDate;

    @Column(name = "stars")
    private int stars;

    @Column(name = "text")
    private String text;

}
