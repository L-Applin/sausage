package help.sausage.ui.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import help.sausage.dto.ReviewDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class ReviewForm<T> extends VerticalLayout {

    public interface ReviewCreatedListener {
        void onNewReview(ReviewDto reviewDto);
    }

    protected final FlexLayout crimBoxHolder = new FlexLayout();
    protected final TextArea reviewArea = new TextArea(null, "Did you have a good kidnapping experience?");
    protected final DatePicker datePicker = new DatePicker(LocalDate.now());
    protected final StarVoteSelectComponent starsVoteComponent = new StarVoteSelectComponent();

    protected List<ReviewFormComponent.ReviewCreatedListener> reviewCreatedListeners = new ArrayList<>();

    public void addOnReviewCreatedListener(ReviewFormComponent.ReviewCreatedListener listener) {
        if (listener != null) {
            reviewCreatedListeners.add(listener);
        }
    }

    public ReviewForm() {
        // Create UI components
        reviewArea.setMaxLength(255);
        setFlexGrow(1, reviewArea);
        reviewArea.setClassName("main-review-from-descr");
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
        createButton.addClickListener(e -> submitNewReview(getData()));
        HorizontalLayout formLayout = new HorizontalLayout(datePicker, crimsField,
                starsVoteComponent, createButton);
        formLayout.setVerticalComponentAlignment(Alignment.CENTER, starsVoteComponent);
        formLayout.setFlexGrow( 2, createButton);
        formLayout.setPadding(false);
        formLayout.setMargin(false);
        formLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        formLayout.getStyle().set("margin", "0px");
        formLayout.setWidth("100%");

        getStyle().set("padding", "0px");

        setHorizontalComponentAlignment(Alignment.CENTER, formLayout, reviewArea);
        setMargin(false);

        add(reviewArea, crimBoxHolder, formLayout);

    }

    protected KnownCrimBtnComponent createCrimBox(String crimName, FlexLayout crimBoxHolder) {
        KnownCrimBtnComponent cmp = new KnownCrimBtnComponent(crimName);
        cmp.setOnClose(e -> crimBoxHolder.remove(cmp));
        return cmp;
    }

    abstract T getData();

    abstract void submitNewReview(T data);
}
