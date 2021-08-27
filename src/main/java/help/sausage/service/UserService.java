package help.sausage.service;

import help.sausage.dto.NewUserDto;
import help.sausage.dto.UserDto;
import help.sausage.entity.UserEntity;
import help.sausage.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto createNewUser(NewUserDto userDto) {
        final UserEntity toSave = fromDto(userDto);
        final UserEntity saved = userRepository.save(toSave);
        return toDto(saved);
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
}
