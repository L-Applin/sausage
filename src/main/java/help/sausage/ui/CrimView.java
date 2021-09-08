package help.sausage.ui;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import help.sausage.client.CriminalClient;
import help.sausage.dto.CrimInfoDto;
import help.sausage.dto.ErrorDto;
import help.sausage.dto.ReviewDto;
import help.sausage.ui.component.CrimInfoComponent;
import help.sausage.ui.component.LeftColumnComponent;
import help.sausage.ui.component.ReviewCardComponent;
import help.sausage.ui.component.RightColumnComponent;
import help.sausage.ui.data.Review;
import help.sausage.utils.ApplicationContextProvider;
import help.sausage.utils.ResponseWrapper;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Route(value = "crim/:crimName")
@CssImport("./styles/crim-view.css")
@Slf4j
public class CrimView extends VerticalLayout
        implements BeforeEnterObserver, HasDynamicTitle {

    private String crimName;
    private final CriminalClient criminalClient =
            ApplicationContextProvider.getCtx().getBean(CriminalClient.class);
    private final ResponseWrapper wrapper =
            ApplicationContextProvider.getCtx().getBean(ResponseWrapper.class);

    @Override
    public String getPageTitle() {
        return "Crim %s| Sausage".formatted(crimName + " ");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        removeAll();
        this.crimName = event.getRouteParameters().get("crimName")
                .orElseThrow();
        wrapper.handleResponse(() -> criminalClient.getCrimInfo(crimName), this::handleResponse, this::handleError);
    }

    private void handleResponse(CrimInfoDto crimInfo) {
        setClassName("main-vew");
        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setClassName("main-view-horizontal-wrapper");
        LeftColumnComponent leftColumn = new LeftColumnComponent();
        RightColumnComponent rightColumn = new RightColumnComponent();
        VerticalLayout center = new VerticalLayout();
        center.setClassName("main-view-center");
        CrimInfoComponent crimInfoComponent = new CrimInfoComponent(crimInfo);
        center.add(crimInfoComponent);

        DataProvider<Review, Void> reviewProvider = crimReviewProvider(crimInfo);
        Grid<Review> crimReviewsGrid = new Grid<>();
        crimReviewsGrid.setId("crim-view-grid");
        crimReviewsGrid.setDataProvider(reviewProvider);
        crimReviewsGrid.addColumn(new ComponentRenderer<>(ReviewCardComponent::new));
        crimReviewsGrid.setDetailsVisibleOnClick(false);
        crimReviewsGrid.setHeightByRows(true);
        crimReviewsGrid.setSelectionMode(SelectionMode.NONE);

        center.add(crimReviewsGrid);
        center.setHorizontalComponentAlignment(Alignment.CENTER, crimReviewsGrid);

        wrapper.add(leftColumn, center, rightColumn);
        wrapper.expand();
        setHorizontalComponentAlignment(Alignment.CENTER, wrapper);
        add(wrapper);

    }

    private void handleError(ErrorDto errorDto) {
        Notification.show("Error while trying to get criminal informations: %s".formatted(errorDto.getMsg()));
    }

    private DataProvider<Review, Void> crimReviewProvider(CrimInfoDto crimInfo) {
        long maxReviews = crimInfo.total();
        return DataProvider.fromCallbacks(query -> {
            log.info("Request from DeataProvider {}:{}", query.getOffset(), query.getLimit());
                    List<ReviewDto> body = criminalClient
                            .getReviewForCrim(crimInfo.username(), query.getOffset(), query.getLimit())
                            .getBody();
                    return body == null
                            ? Stream.empty()
                            : body.stream().map(Review::fromDto);
                },
                query -> (int) maxReviews);
    }
}
