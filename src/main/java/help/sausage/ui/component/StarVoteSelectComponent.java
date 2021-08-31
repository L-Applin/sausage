package help.sausage.ui.component;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import org.w3c.dom.events.MouseEvent;

public class StarVoteSelectComponent extends HorizontalLayout {

    private int amount = 0;
    private final Icon[] icons = new Icon[5];

    public StarVoteSelectComponent() {
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
        createZeroIcon();
        for (int i = 0; i < 5; i++) {
            Icon icon = getIcon(VaadinIcon.STAR_O);
            icons[i] = icon;
        }
        setListeners(icons, true);
        add(icons);
    }

    public void createZeroIcon() {
        Icon zero = VaadinIcon.BAN.create();
        zero.setSize("1em");
        zero.getStyle().set("opacity", "0.5");
        zero.getStyle().set("margin-right", "8px");
        zero.getElement().addEventListener("click", e -> {
            amount = 0;
            for (Icon icon : icons) {
                remove(icon);
            }
            for (int i = 0; i < 5; i++) {
                icons[i] = getIcon(VaadinIcon.STAR_O);
                icons[i].getStyle().set("opacity", "1");
            }
            add(icons);
            setListeners(icons, true);
        });
        zero.getElement().addEventListener("mouseenter", e -> zero.getStyle().set("opacity", "0.85"));
        zero.getElement().addEventListener("mouseleave", e -> zero.getStyle().set("opacity", "0.5"));
        add(zero);
    }

    private Icon getIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.setSize("1em");
        icon.getStyle().set("margin", "0px");
        icon.getStyle().set("padding", "2px");
        icon.setColor("orange");
        return icon;
    }

    public int getAmount() {
        return amount;
    }

    private void setIcons(int iconsNum) {
        setIcons(iconsNum, "1", true);
    }

    private void setIcons(int iconsNum, String opacity, boolean setMouseenterListeners) {
        for (Icon icon : icons) {
            remove(icon);
        }
        for (int i = 0; i <= iconsNum; i++) {
            icons[i] = getIcon(VaadinIcon.STAR);
            icons[i].getStyle().set("opacity", opacity);
        }
        for (int i = iconsNum + 1; i < 5; i++) {
            icons[i] = getIcon(VaadinIcon.STAR_O);
        }
        setListeners(icons, setMouseenterListeners);
        add(icons);
    }

    private void setListeners(Icon[] icons, boolean setMouseenterListeners) {
        for (int i = 0; i < icons.length; i++) {
            Icon icon = icons[i];
            icon.getElement().addEventListener("click", new StarVoteClickListener(i));
            if (setMouseenterListeners) {
                final int finalI = i;
                icon.getElement().addEventListener("mouseenter", e -> {
                    // set icons, without mouse enter event
                    setIcons(finalI, "0.5", false);
                });
            }
            icon.getElement().addEventListener("mouseleave", e -> setIcons(amount - 1, "1", true));
        }
    }

    private class StarVoteClickListener implements DomEventListener {

        private final int starclicked;

        public StarVoteClickListener(int starclicked) {
            this.starclicked = starclicked;
        }

        @Override
        public void handleEvent(DomEvent event) {
            amount = starclicked + 1;
            Notification.show("Clicked on star index: " + starclicked);
            Notification.show("Current total:   " + getAmount());
            setIcons(starclicked, "1", false);
        }
    }

}
