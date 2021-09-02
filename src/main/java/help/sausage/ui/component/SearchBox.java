package help.sausage.ui.component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import java.util.function.Consumer;

@Tag("main-searchbox")
@CssImport("./styles/main-searchbox.css")
public class SearchBox extends Div {
    private Icon icon = VaadinIcon.SEARCH.create();
    private TextField textField = new TextField("", "Search");

    private Consumer<String> callback;

    public SearchBox(Consumer<String> callback) {
        this.callback = callback;
        HorizontalLayout layout = new HorizontalLayout(icon, textField);
        add(layout);
        icon.addClickListener(e -> callback.accept(textField.getValue()));
    }

}
