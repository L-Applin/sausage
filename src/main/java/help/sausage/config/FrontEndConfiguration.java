package help.sausage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;
import java.io.IOException;
import java.lang.runtime.ObjectMethods;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.servlet.http.Cookie;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FrontEndConfiguration {

    public static final String JSESSIONID = "JSESSIONID";

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public RestTemplate frontEndClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        builder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
                Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
        RestTemplate restTemplate = builder.build();
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        restTemplate.getInterceptors().add(new JsessionidInterceptor());
        return restTemplate;
    }

    private static final class JsessionidInterceptor implements ClientHttpRequestInterceptor{
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                ClientHttpRequestExecution execution) throws IOException {
            Cookie[] cookies = VaadinRequest.getCurrent().getCookies();
            Stream.of(cookies).filter(c -> JSESSIONID.equalsIgnoreCase(c.getName()))
                    .findFirst()
                    .ifPresent(c -> request.getHeaders().put("Cookie", List.of("%s=%s".formatted(c.getName(), c.getValue()))));
            return execution.execute(request, body);
        }
    }

}
