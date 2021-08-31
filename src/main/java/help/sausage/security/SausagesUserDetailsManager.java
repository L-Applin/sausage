package help.sausage.security;

import help.sausage.entity.UserEntity;
import help.sausage.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Deprecated
public class SausagesUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .map(this::fromEntity)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private User fromEntity(UserEntity user) {
        return new User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("USER"))
        );
    }

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userRepo.findByUsername(username).isPresent();
    }
}
