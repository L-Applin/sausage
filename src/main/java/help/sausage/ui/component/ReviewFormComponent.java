package help.sausage.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import help.sausage.ui.data.Review;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ReviewFormComponent extends VerticalLayout {



    public ReviewFormComponent() {
        // Create UI components
        TextArea reviewArea = new TextArea(null, "Did you have a good kidnapping experience?");
        reviewArea.setMaxLength(255);
        setFlexGrow(1, reviewArea);
        reviewArea.setWidth("100%");
        reviewArea.setHeight("8em");
        reviewArea.getStyle().set("margin", "0px");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setWidth(9, Unit.EM);
        TextField crimsField = new TextField(null, "Who kidnapped you?");
        FlexLayout crimBoxHolder = new FlexLayout();
        crimBoxHolder.setFlexWrap(FlexWrap.WRAP);
        crimBoxHolder.getStyle().set("margin", "0px");
        crimsField.addKeyDownListener(Key.ENTER, e -> {
            if (!crimsField.isEmpty()) {
                String crimName = crimsField.getValue();
                crimBoxHolder.add(createCrimBox(crimName, crimBoxHolder));
                crimsField.clear();
            }
        });

        StarVoteSelectComponent stars = new StarVoteSelectComponent();
        Button createButton = new Button("Review");
        createButton.setWidth("100%");
        createButton.getElement().addEventListener("click", e -> {
            List<String> crimNames = crimBoxHolder.getChildren().map(el -> ((KnownCrimBtnComponent) el).getCrimName()).toList();
            Notification.show(new NewReviewDto(
                    UUID.randomUUID(), // fixme once login, get from login user
                    crimNames,
                    datePicker.getValue(),
                    stars.getAmount(),
                    reviewArea.getValue())
                .toString());
        });
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

    private KnownCrimBtnComponent createCrimBox(String crimName, FlexLayout crimBoxHolder) {
        KnownCrimBtnComponent cmp = new KnownCrimBtnComponent(crimName);
        cmp.setOnClose(e -> crimBoxHolder.remove(cmp));
        return cmp;
    }



}
