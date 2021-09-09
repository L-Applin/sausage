package help.sausage.repository;

import help.sausage.entity.KnownCrimEntity;
import help.sausage.entity.KnownCrimEntityId;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface KnownCrimRepository extends CrudRepository<KnownCrimEntity, KnownCrimEntityId> {
    void deleteAllByIdReviewId(UUID reviewId);
}
