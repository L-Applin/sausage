package help.sausage.ui.component;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import help.sausage.entity.UserIcon;
import help.sausage.ui.data.SessionUser;

public class RightColumnComponent extends VerticalLayout {

    public RightColumnComponent() {
        SessionUser user = VaadinSession.getCurrent().getAttribute(SessionUser.class);
        HorizontalLayout userLayout = new HorizontalLayout();
        userLayout.setClassName("main-right-user-layout");
        userLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        H2 username = new H2(user.username());
        UserIcon userIcon = UserIcon.from(user.icon());
        Image avatar = new Image(userIcon.asUrl(), userIcon.name());
        avatar.setClassName("main-user-avatar");
        userLayout.add(avatar, username);
        add(userLayout);
    }
}
