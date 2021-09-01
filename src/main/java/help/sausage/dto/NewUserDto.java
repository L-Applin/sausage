package help.sausage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record NewUserDto(
    String username,
    String encodedPwd,
    String icon
){

    public NewUserDto() {
        this(null, null, null);
    }
}
