package help.sausage.ui.data;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

public record SessionUser(
        String username,
        UUID uuid,
        String icon
        )
{}
