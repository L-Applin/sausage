package help.sausage.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record NewUserDto(
    String username,
    String encodedPwd,
    String icon,
    LocalDateTime dateJoined
){
    public NewUserDto() {
        this(null, null, null, null);
    }
}
