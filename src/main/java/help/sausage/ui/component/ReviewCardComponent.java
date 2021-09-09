package help.sausage.ui.component;

import static help.sausage.utils.Null.orElse;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import help.sausage.client.ReviewClient;
import help.sausage.ui.CrimView;
import help.sausage.ui.data.Review;
import help.sausage.ui.data.Review.Crim;
import help.sausage.ui.data.SessionUser;
import help.sausage.utils.ApplicationContextProvider;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Tag("review-card")
@CssImport("./styles/review-card.css")
@Slf4j
public class ReviewCardComponent extends VerticalLayout {

    private Review review;
    private ReviewClient reviewClient;
    private SessionUser sessionUser;
    private HorizontalLayout innerSocial;

    private VerticalLayout likesLayout = new VerticalLayout();
    private boolean likedByUser;
    private VerticalLayout editLayout;
    private Icon commentsIcon;
    private Registration editClickReg;
    private Registration likesRegistration;
    private Registration commentReg;

    public ReviewCardComponent(Review review) {
        this.review = review;
        this.reviewClient = ApplicationContextProvider.getCtx().getBean(ReviewClient.class);
        this.sessionUser = VaadinSession.getCurrent().getAttribute(SessionUser.class);

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

        VerticalLayout social = createSocialsLayout();

        HorizontalLayout authorTop = new HorizontalLayout(authorIcon, author, social);
        authorTop.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        HorizontalLayout sub = new HorizontalLayout(starHolder, date);
        sub.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        Hr hr = new Hr();
        hr.setId("review-card-seperator");
        setHorizontalComponentAlignment(Alignment.CENTER, hr);

        add(authorTop);
        add(sub);
        add(descrContainer);
        add(hr);
        add(crimsHolder);
    }

    private VerticalLayout createSocialsLayout() {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setId("review-card-social-wrapper");

        innerSocial = new HorizontalLayout();
        innerSocial.setId("review-card-social");

        likesLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        likesLayout.setClassName("review-card-social-likes review-card-social-item");
        Icon heart = userLikedReview() ? VaadinIcon.HEART.create() : VaadinIcon.HEART_O.create();
        Label likesAmount = new Label("%d".formatted(review.likes()));
        likesLayout.add(heart, likesAmount);
        likesRegistration = heart.addClickListener(this::doLikeReview);

        VerticalLayout commentsLayout = new VerticalLayout();
        commentsLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        commentsLayout.setClassName("review-card-social-item");
        commentsIcon = VaadinIcon.COMMENT.create();
        Label commentAmount = new Label("%d".formatted(review.comments()));
        commentsLayout.add(commentsIcon, commentAmount);
        commentReg = commentsIcon.addClickListener(this::onCommentClicked);

        if (sessionUser != null && review.author().name().equals(sessionUser.username())) {
            editLayout = new VerticalLayout();
            editLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            editLayout.setClassName("review-card-social-item");
            Icon editIcon = VaadinIcon.PENCIL.create();
            editClickReg = editIcon.addClickListener(this::onEditClicked);
            editLayout.add(editIcon);
            innerSocial.add(editLayout);
        }
        innerSocial.add(commentsLayout, likesLayout);
        wrapper.add(innerSocial);
        wrapper.setHorizontalComponentAlignment(Alignment.END, innerSocial);

        return wrapper;
    }

    public void removeEditIfExist() {
        if (editLayout != null) {
            innerSocial.remove(editLayout);
        }
    }

    public void noClicks() {
        editClickReg.remove();
        likesRegistration.remove();
        commentReg.remove();
    }

    private void onEditClicked(ClickEvent<Icon> event) {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        ReviewCardComponent reviewCard = new ReviewCardComponent(review);
        reviewCard.removeEditIfExist();
        reviewCard.noClicks();
        Icon closeIcon = VaadinIcon.CLOSE.create();
        closeIcon.setId("review-card-edit-close-icon");
        wrapper.setHorizontalComponentAlignment(Alignment.END, closeIcon);
        Dialog dialog = new Dialog();
        closeIcon.addClickListener(e -> dialog.close());
        dialog.add(wrapper);
        dialog.setCloseOnOutsideClick(false);
        dialog.open();
        EditReviewForm editReviewForm = new EditReviewForm(review, dialog);
        wrapper.add(closeIcon, reviewCard, editReviewForm);
        dialog.setMaxWidth(30, Unit.VW);
    }

    private boolean userLikedReview() {
        final SessionUser user = VaadinSession.getCurrent().getAttribute(SessionUser.class);
        if (user == null) {
            // no user logged in, no likes
            return false;
        }
        try {
            Boolean b = reviewClient.hasUserLiked(review.reviewId()).getBody();
            boolean safeB =  orElse(b, false);
            this.likedByUser = safeB;
            return safeB;
        } catch (Exception e) {
            log.warn("Error while fetching if user '{}' has liked review '{}'", user.uuid(), review.reviewId());
            return false;
        }
    }

    private void onCommentClicked(ClickEvent<Icon> event) {
        //todo @Comments
        Notification.show("~~~ TODO: COMMENTS ~~~");
    }

    private void doLikeReview(ClickEvent<Icon> event) {
        if (likedByUser) {
            Notification.show("Already liked! ~~~ TODO: remove like ~~~"); // todo @Likes
            return;
        }
        ResponseEntity<Long> res = reviewClient.sendLike(this.review.reviewId());
        if (!res.getStatusCode().is2xxSuccessful()) {
            Notification.show("Like failed :("); // todo @Exception
            Notification.show(res.toString());
        } else {
            this.likedByUser = true;
            likesLayout.removeAll();
            Icon heart = VaadinIcon.HEART.create();
            Label likesAmount = new Label(review.likes() + 1 + "");
            likesLayout.add(heart, likesAmount);
            heart.addClickListener(this::doLikeReview);
        }
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
