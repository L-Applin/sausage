package help.sausage.client;

import help.sausage.controller.UserController;
import help.sausage.dto.NewUserDto;
import help.sausage.dto.UserDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class UserClient implements UserController {

    private final RestTemplate frontEndClient;

    @Value("${sausage.server.host}")
    private String host;

    @Override
    public ResponseEntity<UserDto> createNewUser(NewUserDto userDto) {
        final String url = host + BASE_URL;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        return frontEndClient.postForEntity(builder.toUriString(), userDto, UserDto.class);
    }

    @Override
    public ResponseEntity<UserDto> getUserByUsername(String username) {
        final String url = host + BASE_URL + "/{username}";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .uriVariables(Map.of("username", username));
        return frontEndClient.getForEntity(builder.toUriString(), UserDto.class);
    }

}
