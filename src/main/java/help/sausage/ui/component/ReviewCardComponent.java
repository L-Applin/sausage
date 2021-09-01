package help.sausage.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
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
import help.sausage.ui.data.Review.Author;
import help.sausage.ui.data.Review.Crim;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/*
style="box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2); border-radius: 1em; background-color: #f0ebeb;
 */
@Tag("review-card")
@CssImport("./styles/review-card.css")
public class ReviewCardComponent extends VerticalLayout {

    public static final Review default_review = new Review(
            new Author("alice", "cat"),
            List.of(new Crim("McQueen", true), new Crim("Jafar", true), new Crim("Unknown", false), new Crim("Lightning", true)),
            LocalDate.of(2021, 8, 3),
            LocalDate.of(2021, 8, 3),
            4,
            """
                Best experience ever <3 ! They did not rob me, and even took the time to put my illegal stuff in a trash can, so that I could get them back later. Would recommend!!!
                
                https://i.imgur.com/pvxVF7R.jpeg
                """);

    public ReviewCardComponent(Review review) {

        H2 author = new H2(review.author().name());
        author.setClassName("review-card-author");

        final String iconName = review.author().icon();
        Image authorIcon = new Image("images/user-icons/%s.png".formatted(iconName),
                "user icon: %s".formatted(iconName));
        authorIcon.setClassName("review-card-user-icon");

        Span date = new Span(review.dateReview().toString());
        date.setClassName("review-card-date");

        Label descr = new Label(review.text());
        Div descrContainer = new Div();
        descrContainer.add(descr);
        descrContainer.setClassName("review-card-text");

        HorizontalLayout starHolder = new HorizontalLayout();
        starHolder.setClassName("review-card-star-holder");

        int totalStars = review.stars();
        for (int i = 1; i <= 5; i++) {
            Icon icon;
            if (i <= totalStars) {
                icon = VaadinIcon.STAR.create();
            } else {
                icon = VaadinIcon.STAR_O.create();
            }
            icon.setClassName("review-card-star-icon");
            starHolder.add(icon);
        }

        List<Component> crims = review.crims().stream()
                .map(this::createLinkOrTextFromCrim)
                .toList();
        FlexLayout crimsHolder = new FlexLayout();
        crimsHolder.setFlexWrap(FlexWrap.WRAP);
        crims.forEach(crimsHolder::add);
        crimsHolder.setClassName("review-card-crim-list");

        HorizontalLayout authorTop = new HorizontalLayout(authorIcon, author);
        authorTop.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        HorizontalLayout sub = new HorizontalLayout(starHolder, date);
        sub.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        add(authorTop);
        add(sub);
        add(descrContainer);
        add(crimsHolder);
    }

    private Component createLinkOrTextFromCrim(Crim crim) {
        String crimName = crim.name();
        Component c = crim.isKnown()
            ? new RouterLink(crimName, CrimView.class, new RouteParameters("crimName", crimName))
            : new Span(crimName);
        ((HasStyle) c).setClassName("review-card-crim-name");
        return c;
    }
}
