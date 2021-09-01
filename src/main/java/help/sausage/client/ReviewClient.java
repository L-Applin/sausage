package help.sausage.client;

import help.sausage.controller.ReviewController;
import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class ReviewClient implements ReviewController {

    public static final ParameterizedTypeReference<List<ReviewDto>> REVIEW_LIST_RESPONSE_TYPE =
            new ParameterizedTypeReference<>() {};

    private final RestTemplate frontEndClient;

    @Value("${sausage.server.host}")
    private String host;

    @Override
    public ResponseEntity<ReviewDto> createNewReview(NewReviewDto reviewDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<ReviewDto>> getAllReviewsPaginated(
            int page, int size, String sortBy, String dir) {
        final String url = host + BASE_URL;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sortBy", sortBy)
                .queryParam("dir", dir);

        return frontEndClient.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                REVIEW_LIST_RESPONSE_TYPE);
    }

    public ResponseEntity<List<ReviewDto>> getAllReviewsPaginated() {
        final String url = host + BASE_URL;
        HttpHeaders httpHeaders = new HttpHeaders();
        List<String> appJson = new ArrayList<>();
        appJson.add(MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.put(HttpHeaders.CONTENT_TYPE, appJson);
        return frontEndClient.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                REVIEW_LIST_RESPONSE_TYPE);
    }

    public ResponseEntity<List<ReviewDto>> getAllReviewsPaginated(
            int page, int size, String sortBy) {
        final String url = host + BASE_URL;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sortBy", sortBy);

        return frontEndClient.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                REVIEW_LIST_RESPONSE_TYPE);
    }

    @Override
    public ResponseEntity<List<ReviewDto>> getReviewByUsername(String username) {
        final String url = host + BASE_URL + GET_REVIEW_BY_USERNAME_URL;
        return frontEndClient.exchange(host + "/v1/review/{username}",
                HttpMethod.GET, HttpEntity.EMPTY, REVIEW_LIST_RESPONSE_TYPE, username);
    }
}
