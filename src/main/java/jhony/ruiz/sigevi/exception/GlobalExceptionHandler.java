package jhony.ruiz.sigevi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------- Autenticación ----------

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex, WebRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos", request);
    }

    @ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<CustomErrorResponse> handleUserDisabled(UserDisabledException ex, WebRequest request) {
        return build(HttpStatus.FORBIDDEN, "Tu cuenta se encuentra deshabilitada", request);
    }

    // ---------- Recursos no encontrados ----------

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleModelNotFound(ModelNotFoundException ex, WebRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // ---------- Duplicados / conflictos de datos ----------

    @ExceptionHandler(CodigoExistException.class)
    public ResponseEntity<CustomErrorResponse> handleCodigoExist(CodigoExistException ex, WebRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    // ---------- Caja (CU08 / RF-06) ----------

    @ExceptionHandler(CajaNoAbiertaException.class)
    public ResponseEntity<CustomErrorResponse> handleCajaNoAbierta(CajaNoAbiertaException ex, WebRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(CajaYaAbiertaException.class)
    public ResponseEntity<CustomErrorResponse> handleCajaYaAbierta(CajaYaAbiertaException ex, WebRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(ObservacionRequeridaException.class)
    public ResponseEntity<CustomErrorResponse> handleObservacionRequerida(ObservacionRequeridaException ex, WebRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // ---------- Ventas / Stock (CU01, RF-11) ----------

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<CustomErrorResponse> handleStockInsuficiente(StockInsuficienteException ex, WebRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    // ---------- Validación de @Valid en los DTOs ----------

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidation(
            org.springframework.web.bind.MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce((a, b) -> a + " | " + b)
                .orElse("Datos inválidos");
        return build(HttpStatus.BAD_REQUEST, message, request);
    }

    // ---------- Helper ----------

    private ResponseEntity<CustomErrorResponse> build(HttpStatus status, String message, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse(
                LocalDateTime.now(),
                message,
                request.getDescription(false)
        );
        return ResponseEntity.status(status).body(error);
    }

    // ---------- Catch-all: cualquier excepción no prevista ----------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGeneric(Exception ex, WebRequest request) {
        log.error("Error no controlado: ", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado en el servidor", request);
    }
}