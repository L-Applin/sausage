package help.sausage.repository;

import help.sausage.entity.AppUserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUserEntity, UUID> {
    Optional<AppUserEntity> findByUsername(String username);
}
