package help.sausage.ui;

import static help.sausage.utils.NotYetImplemented.notYetImplemented;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import help.sausage.client.ReviewClient;
import help.sausage.ui.component.LeftColumnComponent;
import help.sausage.ui.component.ReviewHolderComponent;
import help.sausage.ui.component.RightColumnComponent;
import help.sausage.ui.data.Review;
import help.sausage.utils.ApplicationContextProvider;
import java.util.List;
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
    private ReviewHolderComponent reviewHolder = new ReviewHolderComponent(this::searchFor);

    private final ReviewClient reviewClient;

    public SearchView() {
        this.reviewClient = ApplicationContextProvider.getCtx().getBean(ReviewClient.class);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        List<String> searches = event.getLocation().getQueryParameters().getParameters().get(FULL_TEXT_QUERY_PARAM);
        if (searches != null && searches.size() > 0) {
            this.initfullSearch = searches.get(0);
            log.info("Full text search for '{}'", initfullSearch);
        }

        LeftColumnComponent left = new LeftColumnComponent();
        RightColumnComponent right = new RightColumnComponent();
        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.add(left, center(), right);
        add(wrapper);
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
        checkboxGroup.setItems(List.of("text", "author", "comments"));
        checkboxGroup.select("text", "author");
        searchField.setId("search-form-checkbox-group");
        layout.add(checkboxGroup);

        HorizontalLayout dateWrapper = new HorizontalLayout(startDate, endDate);
        layout.add(dateWrapper);

        return layout;
    }

    private Stream<Review> searchFor(Query<Review, Void> reviewVoidQuery) {
        // todo
        return notYetImplemented();
    }
}
