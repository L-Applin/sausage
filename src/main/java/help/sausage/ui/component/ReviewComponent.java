package help.sausage.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import help.sausage.dto.ReviewDto;
import help.sausage.ui.CrimView;
import help.sausage.ui.data.Review;
import help.sausage.ui.data.Review.Crim;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ReviewComponent extends VerticalLayout {

    public static Review default_review = new Review(
            "alice",
            List.of(new Crim("McQueen", true), new Crim("Jafar", true), new Crim("<This dude I dont remember his name>", false), new Crim("Lightning", true)),
            LocalDate.of(2021, 8, 3),
            4,
            """
                Best experience ever <3 ! They did not rob me, and even took the time to put my illegal stuff in a trash can, so that I could get them back later. Would recommend!!!
                
                https://i.imgur.com/pvxVF7R.jpeg"
                """);

    public ReviewComponent(Review review) {
        setWidth("100%");
        getStyle().set("border-top", "1px solid gray");
        getStyle().set("padding-top", "32px");

        H2 author = new H2(review.authorName());
        author.getStyle().set("margin", "0px");
        author.getStyle().set("padding-left", "16px");
        Span date = new Span(review.date().toString());
        date.getStyle().set("opacity", "0.6");

        Label descr = new Label(review.text());
        descr.getStyle().set("word-wrap", "break-word");
        descr.getStyle().set("margin", "0px");
        List<Component> crims = review.crims().stream()
                .map(this::createLinkOrTextFromCrim)
                .toList();

        HorizontalLayout starHolder = new HorizontalLayout();
        starHolder.setMargin(false);
        starHolder.getStyle().set("margin", "0px");
        int totalStars = review.stars();
        for (int i = 1; i <= 5; i++) {
            Icon icon;
            if (i <= totalStars) {
                icon = VaadinIcon.STAR.create();
            } else {
                icon = VaadinIcon.STAR_O.create();
            }
            icon.setSize("1em");
            icon.getStyle().set("margin", "4px");
            starHolder.add(icon);
        }

        HorizontalLayout top = new HorizontalLayout(author, date);
        top.getStyle().set("margin-top", "0px");
        top.setVerticalComponentAlignment(Alignment.END, author, date);
        FlexLayout crimsHolder = new FlexLayout();
        crimsHolder.setFlexWrap(FlexWrap.WRAP);
        crims.forEach(crimsHolder::add);

        add(top);
        add(starHolder);
        add(descr);
        add(crimsHolder);
    }

    private Component createLinkOrTextFromCrim(Crim crim) {
        String crimName = crim.name();
        Component c = crim.isKnown()
            ? new RouterLink(crimName, CrimView.class, new RouteParameters("crimName", crimName))
            : new Span(crimName);
        c.getElement().getStyle().set("margin-right", "1em");
        return c;
    }
}
