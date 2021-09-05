package help.sausage.repository;

import help.sausage.entity.ReviewLikeEntity;
import help.sausage.entity.ReviewLikeId;
import org.springframework.data.repository.CrudRepository;

public interface ReviewLikeRepository extends CrudRepository<ReviewLikeEntity, ReviewLikeId> {

}
