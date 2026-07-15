package jhony.ruiz.sigevi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "INVALID_CREDENTIALS", "message", "Usuario o contraseña incorrectos"));
    }

    @ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<Map<String, String>> handleUserDisabled(UserDisabledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "USER_DISABLED", "message", "Tu cuenta se encuentra deshabilitada"));
    }
}