package help.sausage.repository;

import help.sausage.entity.CrimInfoEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface CrimInfoRepository extends CrudRepository<CrimInfoEntity, String> {
    Optional<CrimInfoEntity> findByUsername(String username);
}
