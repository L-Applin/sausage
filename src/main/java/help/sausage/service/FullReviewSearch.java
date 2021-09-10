package help.sausage.service;

import static help.sausage.entity.SearchTerms.AUTHOR;
import static help.sausage.entity.SearchTerms.CRIM;
import static help.sausage.entity.SearchTerms.TEXT;
import static java.util.stream.Collectors.toSet;

import com.querydsl.core.BooleanBuilder;
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

    public static BooleanExpression crimName(String username) {
        return review.crims.any().username.eq(username);
    }

    public static Predicate text(String text) {
        return review.text.containsIgnoreCase(text); // todo use mySQL full text serach instead
    }

    public static Predicate getFullSearch(
            Optional<String> fullText,
            List<String> searchTerms,
            Optional<LocalDate> startDate,
            Optional<LocalDate> endDate) {
        BooleanBuilder fullSearch = new BooleanBuilder();
        Set<SearchTerms> terms = searchTerms.stream().filter(SearchTerms.allowedSearchTerms()::contains)
                .map(s -> SearchTerms.valueOf(s.toUpperCase())).collect(toSet());

        if (terms.contains(AUTHOR)) {
            fullText.ifPresent(t -> fullSearch.and(authorName(t)));
        }

        if (terms.contains(CRIM)) {
            fullText.ifPresent(t -> fullSearch.and(crimName(t)));
        }

        if (startDate.isPresent() && endDate.isPresent()) {
            fullSearch.and(between(startDate.get(), endDate.get()));
        } else {
            startDate.ifPresent(d -> fullSearch.and(review.reviewDate.after(d)));
            endDate.ifPresent(d -> fullSearch.and(review.reviewDate.after(d)));
        }

        if (terms.contains(TEXT)) {
            fullText.ifPresent(t -> fullSearch.and(text(t)));
        }

        return fullSearch.getValue();
    }

    // todo comments
}
