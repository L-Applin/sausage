package help.sausage.client;


import help.sausage.controller.CriminalController;
import help.sausage.dto.CrimInfoDto;
import help.sausage.dto.ReviewDto;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class CriminalClient implements CriminalController {

    private final RestTemplate frontEndClient;

    @Value("${sausage.server.host}")
    private String host;

    public static final ParameterizedTypeReference<List<ReviewDto>> REVIEW_LIST_RESPONSE_TYPE =
            new ParameterizedTypeReference<>() {};

    public ResponseEntity<List<ReviewDto>> getReviewForCrim(String username) {
        String url = host + BASE_URL + CRIM_REVIEWS_URL;
        UriComponentsBuilder b = UriComponentsBuilder.fromUriString(url)
                .uriVariables(Map.of("username", username));
        return frontEndClient.exchange(b.toUriString(), HttpMethod.GET, HttpEntity.EMPTY, REVIEW_LIST_RESPONSE_TYPE);
    }

    public ResponseEntity<List<ReviewDto>> getReviewForCrim(String username, int offset, int limit) {
        String url = host + BASE_URL + CRIM_REVIEWS_URL;
        UriComponentsBuilder b = UriComponentsBuilder.fromUriString(url)
                .uriVariables(Map.of("username", username))
                .queryParam("page", offset)
                .queryParam("size", limit);
        return frontEndClient.exchange(b.toUriString(), HttpMethod.GET, HttpEntity.EMPTY, REVIEW_LIST_RESPONSE_TYPE);
    }


    @Override
    public ResponseEntity<List<ReviewDto>> getReviewForCrim(String username, int page, int size,
            String sortBy, String dir) {
        String url = host + BASE_URL + CRIM_REVIEWS_URL;
        UriComponentsBuilder b = UriComponentsBuilder.fromUriString(url)
                .uriVariables(Map.of("username", username))
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sortBy", sortBy)
                .queryParam("dir", dir);
        return frontEndClient.exchange(b.toUriString(), HttpMethod.GET, HttpEntity.EMPTY, REVIEW_LIST_RESPONSE_TYPE);
    }

    @Override
    public ResponseEntity<CrimInfoDto> getCrimInfo(String username) {
        String url = host + BASE_URL + CRIM_INFO_URL;
        UriComponentsBuilder b = UriComponentsBuilder.fromUriString(url)
                .uriVariables(Map.of("username", username));
        return frontEndClient.getForEntity(b.toUriString(), CrimInfoDto.class);
    }
}
