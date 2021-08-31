package help.sausage.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FrontEndConfiguration {

    @Bean
    public RestTemplate frontEndClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        builder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return builder.build();
    }
}
