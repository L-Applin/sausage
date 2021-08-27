package help.sausage.validation;

import help.sausage.dto.NewReviewDto;
import help.sausage.entity.UserEntity;
import help.sausage.exceptions.InvalidNewReviewException;
import help.sausage.repository.UserRepository;
import help.sausage.validation.ValidNewReview.NewRewviewValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.springframework.beans.factory.annotation.Autowired;

@Documented
@Constraint(validatedBy = NewRewviewValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNewReview {

    String message() default "Invalid new review";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class NewRewviewValidator implements ConstraintValidator<ValidNewReview, NewReviewDto> {

        @Autowired
        private UserRepository userRepository;

        @Override
        public boolean isValid(NewReviewDto review, ConstraintValidatorContext ctx) {
            AtomicBoolean isValid = new AtomicBoolean(true);
            ctx.disableDefaultConstraintViolation();
            if (review.getStars() < 0 || review.getStars() > 5) {
                ctx.buildConstraintViolationWithTemplate(
                        "Review stars must be between 0 and 5, but was '%d'"
                                .formatted(review.getStars()))
                        .addConstraintViolation();
                isValid.set(false);
            }

            Optional<UserEntity> authorOpt = userRepository.findById(review.getAuthorId());
            authorOpt.ifPresentOrElse(author -> validateAuthor(review, author, ctx, isValid),
                    () -> ctx.buildConstraintViolationWithTemplate(
                            "Cannot find author with id '%s'".formatted(review.getAuthorId()))
                            .addConstraintViolation());
            return isValid.get();
        }

        private void validateAuthor(NewReviewDto review, UserEntity author, ConstraintValidatorContext ctx, AtomicBoolean isValid) {
            for (String crimName : review.getCrims()) {
                if (crimName == null || "".equals(crimName)) {
                    ctx.buildConstraintViolationWithTemplate("Empty crim value not allowed")
                            .addConstraintViolation();
                    isValid.set(false);
                }
                if (author.getUsername().equals(crimName)) {
                    ctx.buildConstraintViolationWithTemplate(
                            "Review author '%s' cannot also be a criminal in their own hostage situation"
                                    .formatted(author.getUsername()))
                            .addConstraintViolation();
                    isValid.set(false);
                 }
            }
        }

    }
}
