package help.sausage.service;

import help.sausage.dto.CrimInfoDto;
import help.sausage.exceptions.UnknownUsernameException;
import help.sausage.repository.CrimInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriminalService {

    private final CrimInfoRepository crimInfoRepository;

    public CrimInfoDto getCrimInfo(String crimName) {
        return crimInfoRepository.findByUsername(crimName)
                .map(CrimInfoDto::fromEntity)
                .orElseThrow(() -> {
                    final String msg = "Criminal with name '%s' not found".formatted(crimName);
                    return new UnknownUsernameException(msg);
                });
    }
}
