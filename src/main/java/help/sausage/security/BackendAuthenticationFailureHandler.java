package help.sausage.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import help.sausage.dto.ErrorDto;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class BackendAuthenticationFailureHandler
        implements AccessDeniedHandler {

    private ObjectMapper objectMapper;

    public BackendAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ErrorDto error = new ErrorDto(
                exception.getMessage(),
                request.getContextPath());
        response.getOutputStream().print(objectMapper.writeValueAsString(error));
        response.getOutputStream().flush();
    }
}
