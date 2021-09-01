package help.sausage.controller;

import help.sausage.dto.NewUserDto;
import help.sausage.dto.UserDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserController {

    String BASE_URL = "/api/user";

    ResponseEntity<UserDto> createNewUser(NewUserDto userDto);

    ResponseEntity<UserDto> getUserByUsername(String username);
}
