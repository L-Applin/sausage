package help.sausage.service;

import help.sausage.dto.ReviewDto;
import help.sausage.entity.ReviewEntity;
import help.sausage.entity.UserEntity;

public class ReviewMapper {

    public static ReviewDto toDto(ReviewEntity review) {
        return new ReviewDto(
                review.getReviewId(),
                review.getAuthorId().getUsername(),
                review.getCrims().stream().map(UserEntity::getUsername).toList(),
                review.getUnknownCrims().stream().map(c -> c.getId().getCrimName()).toList(),
                review.getDate(),
                review.getStars(),
                review.getText());
    }

}
