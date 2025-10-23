package com.box.dispatch_service.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 1. Handle @Valid on @RequestBody (DTO validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation failed");
        body.put("violations", errors);

        return ResponseEntity.badRequest().body(body);
    }

    // 2. Handle Bean Validation on JPA entities (e.g., @Pattern on Item)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> String.format("%s: %s",
                        violation.getPropertyPath(), violation.getMessage()))
                .collect(Collectors.toList());

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation failed");
        body.put("violations", errors);

        return ResponseEntity.badRequest().body(body);
    }

    // 3. Handle custom ResponseStatusException (your existing logic)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatus(ResponseStatusException ex) {
        Map<String, String> body = Map.of("error", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    // 4. Fallback for any other exception (avoid 500 leak)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        Map<String, String> body = Map.of("error", "Internal server error", ex.getMessage(),  null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
