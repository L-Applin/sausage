package help.sausage.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
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
import help.sausage.client.UserClient;
import help.sausage.dto.NewUserDto;
import help.sausage.dto.UserDto;
import help.sausage.entity.UserIcon;
import help.sausage.utils.ResponseWrapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route("account")
@CssImport("./styles/create-account-view.css")
@PageTitle("New account | Sausage app")
public class NewAccountView extends VerticalLayout {

    public static final int MIN_PWD_LENGTH = 8;
    public static final int MAX_PWD_LENGTH = 64;
    public static final int MIN_USERNAME_LENGTH = 2;
    public static final int MAX_USERNAME_LENGTH = 32;

    private final PasswordField pwd =
            new PasswordField("Password", "%d-%d char".formatted(MIN_PWD_LENGTH, MAX_PWD_LENGTH));
    private final TextField usernameField =
            new TextField("Username", "eg: carlsagan42");
    private final Button btn = new Button("Create user");
    private final Select<ImageSelectItem> iconSelect = new Select<>();

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;
    private final ResponseWrapper responseWrapper;

    private NewUserDto user = new NewUserDto();
    Binder<NewUserDto> binder = new Binder<>();
    private Label status = new Label();

    public NewAccountView(@Autowired UserClient userClient,
            @Autowired PasswordEncoder passwordEncoder,
            @Autowired ResponseWrapper responseWrapper) {
        this.userClient = userClient;
        this.passwordEncoder = passwordEncoder;
        this.responseWrapper = responseWrapper;
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
                .withValidator(new StringLengthValidator(
                        "username must be between %d and %d character".formatted(MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH),
                        MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH))
                .bind(NewUserDto::username, (b, f) -> user = new NewUserDto(f, b.encodedPwd(), b.icon(), b.dateJoined()));

        Binding<NewUserDto, String> pwdBinding = binder.forField(pwd)
                .withValidator(new StringLengthValidator(
                        "password must be between %d and %d character".formatted(MIN_PWD_LENGTH, MAX_PWD_LENGTH),
                        MIN_PWD_LENGTH, MAX_PWD_LENGTH))
                .bind(NewUserDto::username, (b, f) -> user = new NewUserDto(f, b.encodedPwd(), b.icon(), b.dateJoined()));

        usernameField.addValueChangeListener(e -> usernameBinding.validate());
        pwd.addValueChangeListener(e -> pwdBinding.validate());

        status.setVisible(false);
        status.setId("create-account-error");
        add(usernameField, pwd, iconSelect, btn, status);
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
        NewUserDto dto = new NewUserDto(usernameField.getValue(), encodedPwd, iconSelect.getValue().name, LocalDateTime.now());
        responseWrapper.handleResponse(() -> userClient.createNewUser(dto),
            this::onUserCreation,
            e -> {
                status.setVisible(true);
                status.setText(e.getMsg());
            });
    }

    private void onUserCreation(UserDto receivedUser) {
        userClient.login(user.username(), user.encodedPwd());
        btn.getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        Notification.show("Please login with your new user!");
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
