package help.sausage.service;

import static help.sausage.entity.SearchTerms.AUTHOR;
import static help.sausage.entity.SearchTerms.CRIM;
import static help.sausage.entity.SearchTerms.TEXT;
import static help.sausage.utils.Null.orElse;
import static help.sausage.utils.Null.safeOr;
import static java.util.stream.Collectors.toSet;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import help.sausage.entity.QReviewEntity;
import help.sausage.entity.SearchTerms;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FullReviewSearch {

    private static final QReviewEntity review = QReviewEntity.reviewEntity;

    public static BooleanExpression authorName(String username) {
        return review.author.username.eq(username);
    }

    public static BooleanExpression between(LocalDate start, LocalDate end) {
        return review.reviewDate.between(start, end);
    }

    public static BooleanExpression after(LocalDate start) {
        return review.reviewDate.after(start);
    }

    public static BooleanExpression before(LocalDate end) {
        return review.reviewDate.before(end);
    }


    public static BooleanExpression crimName(String username) {
        return review.crims.any().username.eq(username).or(
                review.unknownCrims.any().id.crimName.eq(username)
        );
    }

    public static BooleanExpression text(String text) {
        return review.text.containsIgnoreCase(text); // todo use mySQL full text serach instead
    }

    public static Predicate getFullSearch(
            Optional<String> fullText,
            List<String> searchTerms,
            Optional<LocalDate> startDate,
            Optional<LocalDate> endDate) {
        Set<SearchTerms> terms = searchTerms.stream().filter(SearchTerms.allowedSearchTerms()::contains)
            .map(s -> SearchTerms.valueOf(s.toUpperCase())).collect(toSet());

        BooleanExpression root = null;

        if (fullText.isPresent()) {
            if (terms.contains(TEXT)) {
                root = text(fullText.get());
            }
            if (terms.contains(AUTHOR)) {
                final BooleanExpression expr = authorName(fullText.get());
                root = safeOr(root, expr, r -> r.or(expr));
            }
            if (terms.contains(CRIM)) {
                final BooleanExpression expr = crimName(fullText.get());
                root = root == null ? expr : root.or(expr);
            }
        }
        if (startDate.isPresent() && endDate.isPresent()) {
            final BooleanExpression expr = between(startDate.get(), endDate.get());
            root = root == null ? expr : root.and(expr);
        }
        if (startDate.isPresent()) {
            final BooleanExpression expr = after(startDate.get());
            root = root == null ? expr : root.and(expr);
        }
        if (endDate.isPresent()) {
            final BooleanExpression expr = before(endDate.get());
            root = root == null ? expr : root.and(expr);
        }

        return orElse(root, review.reviewId.isNotNull());
    }

    // todo comments
}
