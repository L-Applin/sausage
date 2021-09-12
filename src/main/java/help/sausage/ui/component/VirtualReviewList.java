package help.sausage.ui.component;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import help.sausage.client.ReviewClient;
import help.sausage.ui.data.Review;
import help.sausage.utils.ApplicationContextProvider;

@CssImport("./styles/review-virtual-list.css")
public class VirtualReviewList extends VirtualList<Review> {

    private final ReviewClient reviewClient;

    public VirtualReviewList(FetchCallback<Review, Void> fetchCallback) {
        this.reviewClient = ApplicationContextProvider.getCtx().getBean(ReviewClient.class);
        setDataProvider(DataProvider.fromCallbacks(fetchCallback, this::maxCount));
        setRenderer(new ComponentRenderer<>(ReviewCardComponent::new));
        setId("main-review-holder");
    }

    public int maxCount(Query<Review, Void> query) {
        Long value = reviewClient.getTotalReviewCount().getBody();
        return value.intValue();
    }

}
