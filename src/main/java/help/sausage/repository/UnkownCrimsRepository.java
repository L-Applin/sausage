package help.sausage.repository;

import help.sausage.entity.UnknownCrimId;
import help.sausage.entity.UnknownCrimEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UnkownCrimsRepository extends CrudRepository<UnknownCrimEntity, UnknownCrimId> {
    List<UnknownCrimEntity> findAllById_ReviewId(UUID reviewId);
}
