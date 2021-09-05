package help.sausage.service;

import help.sausage.dto.AuthorDto;
import help.sausage.dto.ReviewDto;
import help.sausage.entity.ReviewEntity;
import help.sausage.entity.AppUserEntity;

public class ReviewMapper {

    public static ReviewDto toDto(ReviewEntity review, long totalLikes, long totalComments) {
        AuthorDto author = new AuthorDto(review.getAuthor().getUsername(), review.getAuthor().getIcon());
        return new ReviewDto(
                review.getReviewId(),
                author,
                review.getCrims().stream().map(AppUserEntity::getUsername).toList(),
                review.getUnknownCrims().stream().map(c -> c.getId().getCrimName()).toList(),
                review.getDateCreated(),
                review.getReviewDate(),
                review.getStars(),
                totalLikes,
                totalComments,
                review.getText());
    }

}
