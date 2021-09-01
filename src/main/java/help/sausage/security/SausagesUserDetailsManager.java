package help.sausage.security;

import help.sausage.entity.AppUserEntity;
import help.sausage.entity.UserEntity;
import help.sausage.repository.AppUserRepository;
import help.sausage.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Deprecated
public class SausagesUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findById(username)
                .map(this::fromEntity)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private User fromEntity(UserEntity user) {
        return new User(
                user.getUsername(),
                user.getUsername(),
                user.isEnabled(),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("USER"))
        );
    }

    @Override
    public void createUser(UserDetails user) {
        userRepo.save(new UserEntity(user.getUsername(), user.getPassword(), user.isEnabled()));
    }

    @Override
    public void updateUser(UserDetails user) {
        //todo
    }

    @Override
    public void deleteUser(String username) {
        //todo
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        //todo
    }

    @Override
    public boolean userExists(String username) {
        return userRepo.findById(username).isPresent();
    }
}
