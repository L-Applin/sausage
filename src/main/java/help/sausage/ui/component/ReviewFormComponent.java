package help.sausage.ui.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import help.sausage.client.ReviewClient;
import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import help.sausage.ui.data.SessionUser;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;

@CssImport(value="./styles/review-text-field.css", themeFor="vaadin-text-area")
public class ReviewFormComponent extends VerticalLayout {

    public interface ReviewCreatedListener {
        void onNewReview(ReviewDto reviewDto);
    }

    private final FlexLayout crimBoxHolder = new FlexLayout();
    private final TextArea reviewArea = new TextArea(null, "Did you have a good kidnapping experience?");
    private final DatePicker datePicker = new DatePicker(LocalDate.now());
    private final StarVoteSelectComponent stars = new StarVoteSelectComponent();

    private final ReviewClient reviewClient;

    private List<ReviewCreatedListener> reviewCreatedListeners = new ArrayList<>();

    public void addListener(ReviewCreatedListener listener) {
        if (listener != null) {
            reviewCreatedListeners.add(listener);
        }
    }

    public ReviewFormComponent(ReviewClient reviewClient) {
        this.reviewClient = reviewClient;

        // Create UI components
        reviewArea.setMaxLength(255);
        setFlexGrow(1, reviewArea);
        reviewArea.setWidth("100%");
        reviewArea.setHeight("8em");
        reviewArea.getStyle().set("margin", "0px");
        reviewArea.getStyle().set("border-radius", "0px");
        datePicker.setWidth(9, Unit.EM);
        TextField crimsField = new TextField(null, "Who kidnapped you?");
        crimBoxHolder.setFlexWrap(FlexWrap.WRAP);
        crimBoxHolder.getStyle().set("margin", "0px");
        crimsField.addKeyDownListener(Key.ENTER, e -> {
            if (!crimsField.isEmpty()) {
                String crimName = crimsField.getValue();
                crimBoxHolder.add(createCrimBox(crimName, crimBoxHolder));
                crimsField.clear();
            }
        });

        Button createButton = new Button("Review");
        createButton.setWidth("100%");
        createButton.addClickListener(this::onClick);
        HorizontalLayout formLayout = new HorizontalLayout(datePicker, crimsField, stars, createButton);
        formLayout.setVerticalComponentAlignment(Alignment.CENTER, stars);
        formLayout.setFlexGrow( 2, createButton);
        formLayout.setPadding(false);
        formLayout.setMargin(false);
        formLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        formLayout.getStyle().set("margin", "0px");

        getStyle().set("padding", "0px");

        setHorizontalComponentAlignment(Alignment.CENTER, formLayout, reviewArea);
        setMargin(false);

        add(reviewArea, crimBoxHolder, formLayout);
    }

    private void onClick(ClickEvent<Button> event) {
        SessionUser user = VaadinSession.getCurrent().getAttribute(SessionUser.class);
        List<String> crimNames = crimBoxHolder.getChildren()
                .map(el -> ((KnownCrimBtnComponent) el).getCrimName()).toList();
        final NewReviewDto newReview = new NewReviewDto(
                user.uuid(),
                crimNames,
                datePicker.getValue(),
                stars.getAmount(),
                reviewArea.getValue());
        ResponseEntity<ReviewDto> response = reviewClient.createNewReview(newReview);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() != null) {
            Notification.show("error creating new review: %s".formatted(newReview.toString()));
        } else {
            reviewCreatedListeners.forEach(l -> l.onNewReview(response.getBody()));
        }
    }

    private KnownCrimBtnComponent createCrimBox(String crimName, FlexLayout crimBoxHolder) {
        KnownCrimBtnComponent cmp = new KnownCrimBtnComponent(crimName);
        cmp.setOnClose(e -> crimBoxHolder.remove(cmp));
        return cmp;
    }



}
