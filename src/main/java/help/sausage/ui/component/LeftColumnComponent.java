package help.sausage.ui.component;

import static help.sausage.ui.SearchView.FULL_TEXT_QUERY_PARAM;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.VaadinSession;
import help.sausage.ui.data.SessionUser;
import java.util.List;
import java.util.Map;

public class LeftColumnComponent extends VerticalLayout {

    public LeftColumnComponent() {
        SessionUser user = VaadinSession.getCurrent().getAttribute(SessionUser.class);
        if (user != null) loggedIn(user);
    }

    private void loggedIn(SessionUser user) {
        Image logo = new Image("images/sausage-icon.png", "Sausage logo");
        SearchBoxComponent searchBox = new SearchBoxComponent(str -> {
            QueryParameters queryParameters = QueryParameters.simple(Map.of(FULL_TEXT_QUERY_PARAM, str));
            UI.getCurrent().navigate("search", queryParameters);
        });

        final Anchor logoutAnchor = new Anchor("", "Logout");
        H2 logout = new H2(logoutAnchor);
        logout.addClickListener(e -> UI.getCurrent().getPage().setLocation("/logout"));
        List<H2> menuItems = List.of(
                new H2(new Anchor("/", "Home")),
                new H2(new Anchor("profile/"+ user.username(), "Profile")),
                logout);

        setClassName("main-left-column");
        setDefaultHorizontalComponentAlignment(Alignment.END);

        H1 title = new H1("sausages.help");
        Label descr = new Label("The best and only kidnapping experience review platform");
        descr.setClassName("main-left-descr");

        logo.setClassName("main-left-sausage-logo");

        VerticalLayout menu = new VerticalLayout();
        menu.setDefaultHorizontalComponentAlignment(Alignment.END);
        menu.setClassName("main-left-menu");
        menuItems.forEach(item -> {
            menu.add(item);
            menu.setClassName("main-left-menu-item");
        });

        add(logo, title, descr, searchBox, menu);
    }
}
