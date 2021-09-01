package help.sausage.ui;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import help.sausage.ui.data.SessionUser;

@Route("login")
@PageTitle("login | Sausage app")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        Image icon = new Image("images/sausage-icon.png", "Sausage logo");
        icon.setWidth(10, Unit.EM);
        login.setForgotPasswordButtonVisible(false);
        HorizontalLayout createAccount = creatAccountLayout();
        add(icon, login, createAccount);
        login.setAction("login");
        login.addLoginListener(e -> {
            Notification.show("User successfully authenticated: " + e.getUsername());
            VaadinSession.getCurrent().setAttribute(SessionUser.class, new SessionUser(e.getUsername(), null, "")); //todo
        });
    }

    private HorizontalLayout creatAccountLayout() {
        return new HorizontalLayout(
            new Span("Dont have an account?"),
            new Anchor("account", "create a new one!")
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }

}
