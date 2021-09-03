package help.sausage.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record UserDto (
    UUID id,
    String username,
    String icon,
    LocalDateTime dateJoined
) { }
