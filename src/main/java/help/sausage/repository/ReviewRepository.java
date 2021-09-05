package help.sausage.repository;

import help.sausage.entity.ReviewEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends CrudRepository<ReviewEntity, UUID> {
    List<ReviewEntity> findAllByAuthorUsername(String username, Sort sort);
    Page<ReviewEntity> findAll(Pageable page);

    @Query(nativeQuery = true,
            value = "select distinct * from sausage.reviews r natural join sausage.crims_involved c natural join sausage.app_users u where u.username=:crimName")
    List<ReviewEntity> findAllCrimReviewByCrimName(@Param("crimName") String crimName);

    @Query(nativeQuery = true,
            value = "select count(*) from sausage.review_likes r where r.review_id=:reviewId")
    long countNumberOfLikes(@Param("reviewId") String reviewId);

}
