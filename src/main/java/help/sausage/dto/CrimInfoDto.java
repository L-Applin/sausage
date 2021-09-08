package help.sausage.dto;

import help.sausage.entity.CrimInfoEntity;
import java.time.LocalDate;

public record CrimInfoDto(
        String username,
        String icon,
        long total,
        double score,
        LocalDate firstReview,
        LocalDate lastReview)
{
    public static CrimInfoDto fromEntity(CrimInfoEntity entity) {
        return new CrimInfoDto(
                entity.getUsername(),
                entity.getIcon(),
                entity.getTotal(),
                entity.getScore(),
                entity.getFirstReview(),
                entity.getLastReview());
    }

}
