package help.sausage.ui.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import help.sausage.client.ReviewClient;
import help.sausage.ui.data.Review;
import help.sausage.utils.ApplicationContextProvider;
import java.util.Comparator;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;

@CssImport("./styles/review-holder.css")
public class ReviewHolderComponent extends VerticalLayout {

    private static final int DEFAULT_SIZE = 3;

    private final ReviewClient reviewClient;
    private final Button loadMoreBtn = new Button();
    private Query<Review, Void> query;

    @Getter @Setter
    private int offset = 0;
    @Getter @Setter
    private int size = DEFAULT_SIZE;

    private DataProvider<Review, Void> dataProvider;

    public ReviewHolderComponent() {
        this.reviewClient = ApplicationContextProvider.getCtx().getBean(ReviewClient.class);
        this.dataProvider = DataProvider.fromCallbacks(this::queryReview, this::maxCount);
        this.query = createQuery();

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setPadding(false);
        setId("main-review-holder");

        loadMoreBtn.setText("more reviews");
        loadMoreBtn.addClickListener(this::loadMore);
        loadMoreBtn.setId("review-holder-load-btn");

        loadReviews();
    }

    private void loadReviews() {
        dataProvider.fetch(query).forEach(r -> add(new ReviewCardComponent(r)));
        offset += size;
        query = createQuery();
        add(loadMoreBtn);
    }

    private Query<Review, Void> createQuery() {
        return new Query<>(offset, size, QuerySortOrder.desc("dateCreated").build(),
                Comparator.comparing(Review::dateReview), null);
    }

    private int maxCount(Query<Review, Void> query) {
        Long value = reviewClient.getTotalReviewCount().getBody();
        return value == null ? -1 : value.intValue(); //todo @Error how to handle backend error here?
    }

    private Stream<Review> queryReview(Query<Review, Void> query) {
        return reviewClient.getAllReviewsPaginated(query.getPage(), query.getLimit())
                .getBody().stream().map(Review::fromDto); // todo @Error how to handle backend error here ???
    }

    private void loadMore(ClickEvent<Button> event) {
        remove(loadMoreBtn);
        loadReviews();
    }

}
