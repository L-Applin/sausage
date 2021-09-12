package help.sausage.ui;

import static help.sausage.utils.Null.safe;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
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
@CssImport("./styles/search-view.css")
public class SearchView extends VerticalLayout implements BeforeEnterObserver {

    public static final String FULL_TEXT_QUERY_PARAM = "t";
    private String initfullSearch;

    private TextField searchField = new TextField("search");
    private CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
    private DatePicker startDate = new DatePicker("start date");
    private DatePicker endDate = new DatePicker("end date");
    private TextField author = new TextField("author");
    private TextField crim = new TextField("crim");
    private ReviewHolderComponent reviewHolder = new ReviewHolderComponent(this::searchFor, false);

    private ReviewClient reviewClient;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        removeAll();
        List<String> searches = event.getLocation().getQueryParameters().getParameters().get(FULL_TEXT_QUERY_PARAM);
        if (searches != null && searches.size() > 0) {
            final String searchFor = searches.get(0);
            this.initfullSearch = searchFor;
            log.info("Full text search for '{}'", this.initfullSearch);
        }
        this.reviewClient = ApplicationContextProvider.getCtx().getBean(ReviewClient.class);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        LeftColumnComponent left = new LeftColumnComponent();
        RightColumnComponent right = new RightColumnComponent();
        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.add(left, center(), right);
        add(wrapper);
        if (initfullSearch != null && !initfullSearch.equals("")) {
            reviewHolder.loadReviews();
        }
    }

    private VerticalLayout center() {
        VerticalLayout layout = new VerticalLayout();
        layout.setClassName("main-view-center");
        layout.setId("search-view-center");

        VerticalLayout form = new VerticalLayout();
        form.setId("search-view-form");

        if (initfullSearch != null) {
            searchField.setValue(initfullSearch);
        }
        searchField.setId("search-form-text-search");
        searchField.setClassName("search-view-full-width");
        form.add(searchField);

        checkboxGroup.setLabel("Search terms");
        checkboxGroup.setItems(List.of("text", "author", "crim", "comments"));
        checkboxGroup.select("text", "author", "crim");
        checkboxGroup.setId("search-form-checkbox-group");
        form.add(checkboxGroup);

        form.add(new Hr());

        startDate.setClassName("search-view-full-width");
        endDate.setClassName("search-view-full-width");
        author.setClassName("search-view-full-width");
        endDate.setClassName("search-view-full-width");
        HorizontalLayout dateWrapper = new HorizontalLayout(startDate, endDate);
        form.add(dateWrapper);
        HorizontalLayout h = new HorizontalLayout();
        h.add(author);
        h.add(crim);
        form.add(h);
        Button searchBtn = new Button("Search");
        searchBtn.setId("search-view-btn");
        searchBtn.addClickListener(e -> {
            reviewHolder.reset();
            reviewHolder.loadReviews();
        });
        h.add(searchBtn);
        form.setHorizontalComponentAlignment(Alignment.END, searchBtn);
        form.add(new Hr());
        layout.add(form, reviewHolder);
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
                query.getPageSize()).getBody();
        return safe(body).map(Review::fromDto); // todo @Error how to handle null body here ??
    }
}
