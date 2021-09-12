package help.sausage.client;

import help.sausage.controller.ReviewController;
import help.sausage.dto.NewReviewDto;
import help.sausage.dto.ReviewDto;
import help.sausage.dto.ReviewUpdateDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
        final String url = host + BASE_URL;
        HttpEntity<NewReviewDto> body = new HttpEntity<>(reviewDto);
        return frontEndClient.exchange(url, HttpMethod.POST, body, ReviewDto.class);
    }

    @Override
    public ResponseEntity<List<ReviewDto>> getAllReviewsPaginated(
            int page, int size, String sortBy, String dir) {
        final String url = host + BASE_URL;
        UriComponentsBuilder builder = builderWithPageParam(url, page, size, sortBy, dir);
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
            int page, int size) {
        final String url = host + BASE_URL;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("page", page)
                .queryParam("size", size);

        return frontEndClient.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                REVIEW_LIST_RESPONSE_TYPE);
    }

    @Override
    public ResponseEntity<ReviewDto> getReviewById(UUID reviewId) {
        final String url = host + BASE_URL + GET_REVIEW_BY_ID_URL;
        Map<String, Object> uriVar = new HashMap<>();
        uriVar.put("reviewId", reviewId.toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .uriVariables(uriVar);
        return frontEndClient.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                ReviewDto.class);
    }

    @Override
    public ResponseEntity<Long> sendLike(UUID reviewId) {
        final String templateUrl = host + BASE_URL + PATCH_SEND_LIKE_URL;
        Map<String, Object> uriVar = new HashMap<>();
        uriVar.put("reviewId", reviewId.toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(templateUrl)
                .uriVariables(uriVar);
        return frontEndClient.exchange(builder.toUriString(),
                HttpMethod.PATCH,
                HttpEntity.EMPTY,
                Long.class);
    }

    @Override
    public ResponseEntity<Boolean> hasUserLiked(UUID reviewId) {
        final String templateUrl = host + BASE_URL + HAS_LIKED_REVIEWD_URL;
        Map<String, Object> uriVar = new HashMap<>();
        uriVar.put("reviewId", reviewId.toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(templateUrl)
                .uriVariables(uriVar);
        return frontEndClient.exchange(builder.toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Boolean.class);
    }

    @Override
    public ResponseEntity<ReviewDto> updateReview(UUID reviewId, ReviewUpdateDto reviewDto) {
        final String templateUrl = host + BASE_URL + PATCH_REVIEW_URL;
        HttpEntity<ReviewUpdateDto> body = new HttpEntity<>(reviewDto);
        Map<String, Object> uriVar = new HashMap<>();
        uriVar.put("reviewId", reviewId.toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(templateUrl)
                .uriVariables(uriVar);
        return frontEndClient.exchange(builder.toUriString(),
                HttpMethod.PATCH,
                body,
                ReviewDto.class);
    }

    @Override
    public ResponseEntity<Long> getTotalReviewCount() {
        final String url = host + BASE_URL + GET_TOTAL_REVIEW_COUNT_URL;
        return frontEndClient.getForEntity(url, Long.class);
    }

    @Override
    public ResponseEntity<List<ReviewDto>> searchReview(Optional<String> fullText,
            List<String> searchTerms, Optional<LocalDate> startDate, Optional<LocalDate> endDate,
            Optional<String> author, Optional<String> crims,
            int page, int size, String sortBy, String dir) {
        final String url = host + BASE_URL + GET_SEARCH;
        UriComponentsBuilder builder = builderWithPageParam(url, page, size, sortBy, dir);
        setupSearchParam(builder, fullText, searchTerms, startDate, endDate, author, crims);
        return frontEndClient.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                REVIEW_LIST_RESPONSE_TYPE);
    }

    public ResponseEntity<List<ReviewDto>> searchReview(Optional<String> fullText,
            List<String> searchTerms, Optional<LocalDate> startDate, Optional<LocalDate> endDate,
            int page, int size) {
        final String url = host + BASE_URL + GET_SEARCH;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("page", page)
                .queryParam("size", size);
        setupSearchParam(builder, fullText, searchTerms, startDate, endDate, Optional.empty(), Optional.empty());
        return frontEndClient.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                REVIEW_LIST_RESPONSE_TYPE);
    }

    public ResponseEntity<List<ReviewDto>> searchReview(Optional<String> fullText,
            List<String> searchTerms, Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
        final String url = host + BASE_URL + GET_SEARCH;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        setupSearchParam(builder, fullText, searchTerms, startDate, endDate, Optional.empty(), Optional.empty());
        return frontEndClient.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                REVIEW_LIST_RESPONSE_TYPE);
    }

    private void setupSearchParam(UriComponentsBuilder builder, Optional<String> fullText,
        List<String> searchTerms, Optional<LocalDate> startDate, Optional<LocalDate> endDate,
            Optional<String> author, Optional<String> crim) {
        fullText.ifPresent(str -> builder.queryParam("t", str));
        if (!searchTerms.isEmpty()) {
            builder.queryParam("in", searchTerms);
        }
        startDate.ifPresent(d -> builder.queryParam("startDate", startDate));
        endDate.ifPresent(d -> builder.queryParam("endDate", endDate));
        author.ifPresent(a -> builder.queryParam("a", a));
        crim.ifPresent(c -> builder.queryParam("c", c));
    }

    private UriComponentsBuilder builderWithPageParam(String url, int page, int size, String sortBy, String dir) {
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sortBy", sortBy)
                .queryParam("dir", dir);
    }

}
