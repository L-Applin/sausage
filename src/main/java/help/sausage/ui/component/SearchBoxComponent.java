package help.sausage.ui.component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import java.util.function.Consumer;

@Tag("main-searchbox")
@CssImport(value = "./styles/main-searchbox.css")
@CssImport(value = "./styles/main-searchbox-text.css", themeFor="vaadin-text-field")
public class SearchBoxComponent extends Div {
    private Icon icon = VaadinIcon.SEARCH.create();
    private TextField textField = new TextField("", "Search");

    public SearchBoxComponent(Consumer<String> callback) {
        icon.setId("searchbox-icon");
        textField.setClassName("transparent-searchbox");
        HorizontalLayout layout = new HorizontalLayout(icon, textField);
        layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(layout);
        icon.addClickListener(e -> callback.accept(textField.getValue()));
    }

}
