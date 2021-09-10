package help.sausage.entity;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// text", "author", "comments"
public enum SearchTerms {
    TEXT,
    AUTHOR,
    CRIM,
    COMMENT;

    public static Set<String> allowedSearchTerms() {
        return Stream.of(SearchTerms.values())
                .map(s -> s.toString().toLowerCase())
                .collect(Collectors.toSet());
    }
}
