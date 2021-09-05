package help.sausage.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.notification.Notification;
import help.sausage.dto.ErrorDto;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@Component
public class ResponseWrapper {

    private ObjectMapper objectMapper;

    public ResponseWrapper() {
        this.objectMapper = new ObjectMapper();
    }

    public <T> void handleResponse(Supplier<ResponseEntity<T>> res,
            Consumer<T> onSuccess, Consumer<ErrorDto> onError) {
        try {
            ResponseEntity<T> responseEntity = res.get();
            onSuccess.accept(responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            try {
               ErrorDto errorDto = objectMapper.readValue(e.getResponseBodyAsByteArray(), ErrorDto.class);
               log.warn("4xx error received from server: {}", errorDto.toString());
               onError.accept(errorDto);
            } catch (Exception ex) {
                log.error("Server error 500 received from server", e);
                Notification.show("Fatal error received from server.");
            }
        } catch (HttpServerErrorException e) {
            log.error("Server error 500 received from server", e);
            Notification.show("Fatal error received from server.");
        }
    }

}
