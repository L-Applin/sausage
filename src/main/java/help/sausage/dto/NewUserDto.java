package help.sausage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {
    private String saltedPwd;
    private String salt;
    private String username;
}
