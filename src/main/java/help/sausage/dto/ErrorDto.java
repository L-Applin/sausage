package help.sausage.dto;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    private String msg;
    private String path;
    private Date timestamp = new Date();
    private Map<String, Object> meta = new HashMap<>();

    public ErrorDto(String msg, String path) {
        this.msg = msg;
        this.path = path;
    }
}
