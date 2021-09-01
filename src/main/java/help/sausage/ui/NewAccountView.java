package help.sausage.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.Binding;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import help.sausage.client.UserClient;
import help.sausage.dto.NewUserDto;
import help.sausage.dto.UserDto;
import help.sausage.entity.UserIcon;
import help.sausage.ui.data.SessionUser;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route("account")
@CssImport("./styles/create-account-view.css")
@PageTitle("New account | Sausage app")
public class NewAccountView extends VerticalLayout {

    public static final int MIN_PWD_LENGTH = 8;
    public static final int MAX_PWD_LENGTH = 64;

    private final PasswordField pwd =
            new PasswordField("Password", "%d-%d char".formatted(MIN_PWD_LENGTH, MAX_PWD_LENGTH));
    private final TextField usernameField =
            new TextField("Username", "eg: carlsagan42");
    private final Button btn = new Button("Create user");
    private final Select<ImageSelectItem> iconSelect = new Select<>();

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;

    private NewUserDto user = new NewUserDto();
    Binder<NewUserDto> binder = new Binder<>();


    public NewAccountView(@Autowired UserClient userClient, @Autowired PasswordEncoder passwordEncoder) {
        this.userClient = userClient;
        this.passwordEncoder = passwordEncoder;
        pwd.setMinLength(MIN_PWD_LENGTH);
        pwd.setMaxLength(MAX_PWD_LENGTH);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        List<ImageSelectItem> icons = Stream.of(UserIcon.values()).map(ImageSelectItem::fromIcon).toList();
        iconSelect.setClassName("create-account-icon-selector");
        iconSelect.setRenderer(new ComponentRenderer<>(img -> img.icon));
        iconSelect.setItems(icons);
        iconSelect.setValue(icons.get(0));

        btn.addClickListener(this::createBtnClickListener);
        btn.setClassName("create-account-btn");

        Binding<NewUserDto, String> usernameBinding = binder.forField(usernameField)
                .withValidator(new StringLengthValidator("username must be between 4 and 128 character", 4, 128))
                .bind(NewUserDto::username, (b, f) -> user = new NewUserDto(f, b.encodedPwd(), b.icon()));

        Binding<NewUserDto, String> pwdBinding = binder.forField(pwd)
                .withValidator(new StringLengthValidator("password must be between %d and %d character".formatted(MIN_PWD_LENGTH, MAX_PWD_LENGTH),
                        MIN_PWD_LENGTH, MAX_PWD_LENGTH))
                .bind(NewUserDto::username, (b, f) -> user = new NewUserDto(f, b.encodedPwd(), b.icon()));

        usernameField.addValueChangeListener(e -> usernameBinding.validate());
        pwd.addValueChangeListener(e -> pwdBinding.validate());

        add(usernameField, pwd, iconSelect, btn);
    }

    private void createBtnClickListener(ClickEvent<Button> event) {
        if (binder.writeBeanIfValid(user)) {
            sendNewUser();
        } else {
            BinderValidationStatus<NewUserDto> status = binder.validate();
            String errorText = status.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
            Notification.show(errorText);
        }
    }

    private void sendNewUser() {
        String encodedPwd = passwordEncoder.encode(pwd.getValue());
        NewUserDto dto = new NewUserDto(usernameField.getValue(), encodedPwd, iconSelect.getValue().name);
        ResponseEntity<?> res = userClient.createNewUser(dto);
        Notification.show(res.toString());
        if (res.getStatusCode().is2xxSuccessful() && res.getBody() != null) {
            UserDto user = (UserDto) res.getBody();
            VaadinSession session = VaadinSession.getCurrent();
            session.setAttribute(SessionUser.class, new SessionUser(user.username(), user.id(), user.icon()));
            btn.getUI().ifPresent(ui -> ui.navigate(MainView.class));
        } else {
            Notification.show(res.toString());
        }
    }

    private record ImageSelectItem(Image icon, String name) {
        public static ImageSelectItem fromIcon(UserIcon userIcon) {
            final Image icon = new Image(userIcon.asUrl(), userIcon.name());
            icon.setClassName("create-account-icon");
            icon.getStyle().set("width", "3em");
            icon.getStyle().set("padding-right", "8px");
            return new ImageSelectItem(icon, userIcon.name());
        }
    }

}
