package help.sausage.service;

import help.sausage.dto.NewUserDto;
import help.sausage.dto.UserDto;
import help.sausage.entity.UserEntity;
import help.sausage.repository.UserRepository;
import help.sausage.validation.UsernameAlreadyExistException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JdbcUserDetailsManager userDetailsManager;

    @Transactional
    public UserDto createNewUser(NewUserDto userDto) {
        validateNewUser(userDto);
        UserDetails userDetails = new User(userDto.getUsername(), userDto.getSaltedPwd(), List.of(new SimpleGrantedAuthority("USER")));
        userDetailsManager.createUser(userDetails);
        UserEntity toSave = fromDto(userDto);
        UserEntity saved = userRepository.save(toSave);
        return toDto(saved);
    }

    private void validateNewUser(NewUserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistException(userDto);
        }
    }

    private UserEntity fromDto(NewUserDto userDto) {
        return new UserEntity(null, userDto.getUsername(), userDto.getSaltedPwd(), userDto.getSalt());
    }

    private UserDto toDto(UserEntity userEntity) {
        return new UserDto(userEntity.getUserId(), userEntity.getUsername());
    }

    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::toDto);
    }

    public void createNewUser(String username, String rawPwd) {
    }
}
