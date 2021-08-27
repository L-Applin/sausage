package help.sausage.repository;

import help.sausage.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {
    Optional<UserEntity>    findByUsername(String username);
}
