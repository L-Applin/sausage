package help.sausage.controller;

import help.sausage.dto.NewUserDto;
import help.sausage.dto.UserDto;
import help.sausage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.BASE_URL)
@RequiredArgsConstructor
public class UserRestController implements UserController {

    private final UserService userService;

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createNewUser(@RequestBody NewUserDto userDto) {
        UserDto created = userService.createNewUser(userDto);
        return ResponseEntity.ok(created);
    }

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
