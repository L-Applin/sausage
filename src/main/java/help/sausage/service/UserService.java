package help.sausage.service;

import help.sausage.dto.NewUserDto;
import help.sausage.dto.UserDto;
import help.sausage.entity.AppUserEntity;
import help.sausage.repository.AppUserRepository;
import help.sausage.exceptions.UsernameAlreadyExistException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository appUserRepository;
    private final JdbcUserDetailsManager userDetailsManager;

    @Transactional
    public UserDto createNewUser(NewUserDto userDto) {
        validateNewUser(userDto);
        UserDetails userDetails = new User(userDto.username(), userDto.encodedPwd(), List.of(new SimpleGrantedAuthority("USER")));
        userDetailsManager.createUser(userDetails);
        AppUserEntity toSave = fromDto(userDto);
        AppUserEntity saved = appUserRepository.save(toSave);
        return toDto(saved);
    }

    private void validateNewUser(NewUserDto userDto) {
        if (appUserRepository.findByUsername(userDto.username()).isPresent()) {
            throw new UsernameAlreadyExistException(userDto);
        }
    }

    private AppUserEntity fromDto(NewUserDto userDto) {
        return new AppUserEntity(null, userDto.username(), userDto.icon());
    }

    private UserDto toDto(AppUserEntity appUserEntity) {
        return new UserDto(appUserEntity.getUserId(),
                appUserEntity.getUsername(),
                appUserEntity.getIcon());
    }

    public Optional<UserDto> getUserByUsername(String username) {
        return appUserRepository.findByUsername(username).map(this::toDto);
    }

    public void createNewUser(String username, String rawPwd) {
    }
}
