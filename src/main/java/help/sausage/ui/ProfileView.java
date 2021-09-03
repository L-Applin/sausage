package help.sausage.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "profile/:username")
public class ProfileView extends VerticalLayout
        implements BeforeEnterObserver, HasDynamicTitle {

    private String username;

    @Override
    public String getPageTitle() {
        return "Profile %s| Sausage".formatted(username + " ");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.username = event.getRouteParameters().get("username")
                .orElseThrow();
        add(new H1(username));
    }


}
