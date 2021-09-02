package help.sausage.ui.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.DomEventListener;

public class KnownCrimBtnComponent extends Div {

    private Button btn;
    private String crimName;
    public String getCrimName() { return this.crimName; }

    public KnownCrimBtnComponent(String name) {
        this.crimName = name;
        Icon closeIcon = new Icon(VaadinIcon.CLOSE_CIRCLE);
        this.btn = new Button();
        Label label = new Label(name);
        label.getStyle().set("padding-left", "0.5em");
        label.getStyle().set("padding-right", "0.5em");
        this.btn.getElement().appendChild(label.getElement());
        this.btn.getElement().appendChild(closeIcon.getElement());

//        getStyle().set("border", "1px solid gray");
        btn.getStyle().set("padding", "0px");
        btn.getStyle().set("spacing", "0px");

        getStyle().set("padding-left",  "4px");
        getStyle().set("padding-right", "4px");
        add(btn);
    }

    public void setOnClose(DomEventListener onClose) {
        btn.getElement().addEventListener("click", onClose);
    }

}
