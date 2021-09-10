package help.sausage.repository;

import help.sausage.entity.ReviewEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository
        extends JpaRepository<ReviewEntity, UUID>, QuerydslPredicateExecutor<ReviewEntity> {
    List<ReviewEntity> findAllByAuthorUsername(String username, Sort sort);

    @Query(nativeQuery = true,
            value =
            """
            select distinct r.review_id, r.author_id, r.date_created as date_created, r.date_review, r.stars, r.text 
              from sausage.reviews r
              natural join sausage.crims_involved 
              natural join (select username from sausage.app_users) as u 
              where u.username=:crimName
            """)
    List<ReviewEntity> findAllCrimReviewByCrimName(@Param("crimName") String crimName, Pageable pageable);

    List<ReviewEntity> findAllByCrimsUsername(String username, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select count(*) from sausage.review_likes r where r.review_id=:reviewId")
    long countNumberOfLikes(@Param("reviewId") String reviewId);


}
