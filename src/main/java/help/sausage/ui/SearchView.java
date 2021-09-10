package help.sausage.ui;

import static help.sausage.utils.Null.safe;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import help.sausage.client.ReviewClient;
import help.sausage.dto.ReviewDto;
import help.sausage.ui.component.LeftColumnComponent;
import help.sausage.ui.component.ReviewHolderComponent;
import help.sausage.ui.component.RightColumnComponent;
import help.sausage.ui.data.Review;
import help.sausage.utils.ApplicationContextProvider;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Route("search")
@PageTitle("Sausage app | Search result")
@Slf4j
public class SearchView extends VerticalLayout implements BeforeEnterObserver {

    public static final String FULL_TEXT_QUERY_PARAM = "t";
    private String initfullSearch;

    private TextField searchField = new TextField("search");
    private CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
    private DatePicker startDate = new DatePicker("start date");
    private DatePicker endDate = new DatePicker("end date");
    private ReviewHolderComponent reviewHolder = new ReviewHolderComponent(this::searchFor, false);

    private ReviewClient reviewClient;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        List<String> searches = event.getLocation().getQueryParameters().getParameters().get(FULL_TEXT_QUERY_PARAM);
        if (searches != null && searches.size() > 0) {
            this.initfullSearch = searches.get(0);
            log.info("Full text search for '{}'", initfullSearch);
        }
        this.reviewClient = ApplicationContextProvider.getCtx().getBean(ReviewClient.class);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        LeftColumnComponent left = new LeftColumnComponent();
        RightColumnComponent right = new RightColumnComponent();
        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.add(left, center(), right);
        add(wrapper);
        reviewHolder.loadReviews();
    }

    private VerticalLayout center() {
        VerticalLayout layout = new VerticalLayout();
        layout.setClassName("main-view-center");

        if (initfullSearch != null) {
            searchField.setValue(initfullSearch);
        }
        searchField.setId("search-form-text-search");
        layout.add(searchField);

        checkboxGroup.setLabel("Search terms");
        checkboxGroup.setItems(List.of("text", "author", "crim", "comment"));
        checkboxGroup.select("text", "author", "crim");
        searchField.setId("search-form-checkbox-group");
        layout.add(checkboxGroup);

        HorizontalLayout dateWrapper = new HorizontalLayout(startDate, endDate);
        layout.add(dateWrapper);
        Button search = new Button();
        search.addClickListener(e -> reviewHolder.loadReviews());
        layout.add(search);
        layout.add(new Hr());
        return layout;
    }

    private Stream<Review> searchFor(Query<Review, Void> query) {
        if (this.reviewClient == null) {
            this.reviewClient = ApplicationContextProvider.getCtx().getBean(ReviewClient.class);
        }
        final String searchFieldValue = searchField.getValue();
        Optional<String> textSearch = searchFieldValue == null || "".equals(searchFieldValue)
                ? Optional.empty()
                : Optional.of(searchFieldValue);
        List<String> searchTerms = checkboxGroup.getSelectedItems().stream().toList();
        Optional<LocalDate> startDateValue = startDate.getOptionalValue();
        Optional<LocalDate> endDateValue = endDate.getOptionalValue();
        List<ReviewDto> body = reviewClient.searchReview(
                textSearch, searchTerms, startDateValue, endDateValue, query.getPage(),
                query.getOffset()).getBody();
        return safe(body).map(Review::fromDto); // todo @Error how to handle null body here ??
    }
}
