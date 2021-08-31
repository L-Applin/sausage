package help.sausage.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import help.sausage.client.UserClient;
import help.sausage.dto.NewUserDto;
import help.sausage.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@Route("account")
public class NewAccountView extends VerticalLayout {

    private final UserClient userClient;

    public NewAccountView(@Autowired UserClient userClient) {
        this.userClient = userClient;
        setDefaultHorizontalComponentAlignment(Alignment.START);
        TextField usernameField = new TextField("Username", "john doe");
        PasswordField pw = new PasswordField();
        pw.setMinLength(8);
        pw.setMaxLength(64);
        Button btn = new Button("Create");
        btn.addClickListener(e -> {
            NewUserDto dto = new NewUserDto(usernameField.getValue(), pw.getValue(), "");
            ResponseEntity<UserDto> res = userClient.createNewUser(dto);
            Notification.show(res.toString());
        });
        add(usernameField, pw, btn);
    }

}
