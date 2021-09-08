package help.sausage.ui.component;

import static help.sausage.utils.MathUtils.roundToHalf;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import help.sausage.dto.CrimInfoDto;
import help.sausage.entity.UserIcon;
import help.sausage.utils.ApplicationContextProvider;
import help.sausage.utils.ResponseWrapper;

public class CrimInfoComponent extends VerticalLayout {

    private ResponseWrapper wrapper = ApplicationContextProvider.getCtx().getBean(ResponseWrapper.class);

    public CrimInfoComponent(CrimInfoDto crimInfo) {
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        HorizontalLayout crimHeader = crimHeader(crimInfo);
        VerticalLayout crimStats = createCrimStats(crimInfo);
        add(crimHeader, crimStats);
    }

    private HorizontalLayout crimHeader(CrimInfoDto infos) {
        HorizontalLayout crimHeader = new HorizontalLayout();
        crimHeader.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Label crimNameLabel = new Label(infos.username());
        final UserIcon icon = UserIcon.from(infos.icon());
        Image userIcon = new Image(icon.asUrl(), icon.name());
        crimNameLabel.setId("crim-info-name");
        userIcon.setId("crim-info-img");
        crimHeader.setId("crim-info-header");
        crimHeader.add(userIcon, crimNameLabel);
        return crimHeader;
    }

    private HorizontalLayout infoLabel(String name, Object value) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setClassName("crim-info-item");

        Label label = new Label(name);
        label.setClassName("crim-info-item-name");

        Label valueLabel = new Label(value.toString());
        valueLabel.setClassName("crim-info-item-value");

        layout.add(label, valueLabel);
        return layout;
    }

    private VerticalLayout createCrimStats(CrimInfoDto infos) {

        double roundedHelfScore = roundToHalf(infos.score());
        int fullStars = (int) roundedHelfScore;
        boolean halfStars = Double.compare(fullStars, roundedHelfScore) != 0;

        HorizontalLayout starWrapper = new HorizontalLayout();
        starWrapper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        starWrapper.setId("crim-info-item-stars-wrapper");

        for (int i = 0; i < fullStars; i++) {
            final Icon starIcon = VaadinIcon.STAR.create();
            starIcon.setClassName("crim-info-item-stars-icon");
            starWrapper.add(starIcon);
        }

        int extraStars = 5 - fullStars;
        if (halfStars) {
            final Icon starIcon = VaadinIcon.STAR_HALF_LEFT_O.create();
            starIcon.setClassName("crim-info-item-stars-icon");
            starWrapper.add(starIcon);
            extraStars -= 1;
        }

        for (int i = 0; i < extraStars; i++) {
            final Icon starIcon = VaadinIcon.STAR_O.create();
            starIcon.setClassName("crim-info-item-stars-icon");
            starWrapper.add(starIcon);
        }

        VerticalLayout crimStats = new VerticalLayout();
        crimStats.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        crimStats.setId("crim-info-stats");
        HorizontalLayout total = infoLabel("Total", infos.total() + " kidnapping" + (infos.total() > 1 ? "s":""));
        HorizontalLayout avg = infoLabel("Average score", infos.score());
        avg.add(starWrapper);
        HorizontalLayout first = infoLabel("First kidnapping", infos.firstReview());
        HorizontalLayout last = infoLabel("Last kidnapping", infos.lastReview());
        crimStats.add(total, avg, first, last);
        return crimStats;
    }


}
