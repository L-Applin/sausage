package help.sausage.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        String msg = "Validation failed for argument ["
                + ex.getParameter().getParameterIndex() + "] in "
                + ex.getParameter().getExecutable().toGenericString();
        ErrorModel model = new ErrorModel(msg, request.getContextPath());
        model.meta.put("target", ex.getTarget());
        model.meta.put("violations", ex.getAllErrors().stream().map(MessageSourceResolvable::getDefaultMessage).toList());
        return ResponseEntity.status(status).body(model);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ErrorModel {
        private String msg;
        private String path;
        private Date timestamp = new Date();
        private Map<String, Object> meta = new HashMap<>();

        public ErrorModel(String msg, String path) {
            this.msg = msg;
            this.path = path;
        }
    }
}
