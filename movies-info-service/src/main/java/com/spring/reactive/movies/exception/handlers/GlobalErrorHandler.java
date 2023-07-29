package com.spring.reactive.movies.exception.handlers;

import static java.util.Objects.nonNull;

import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class GlobalErrorHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> onMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    final ErrorResponse errorResponse =
        new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST);
    errorResponse.setErrors(
        ex.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.toUnmodifiableMap(
                    FieldError::getField,
                    fieldError ->
                        nonNull(fieldError.getDefaultMessage())
                            ? fieldError.getDefaultMessage()
                            : "invalid value",
                    (a, b) -> a + ", " + b)));
    return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> onConstraintViolationException(
      ConstraintViolationException ex) {
    final ErrorResponse errorResponse = new ErrorResponse("Invalid data", HttpStatus.BAD_REQUEST);
    errorResponse.setErrors(
        ex.getConstraintViolations().stream()
            .collect(
                Collectors.toUnmodifiableMap(
                    violation -> violation.getPropertyPath().toString(),
                    violation ->
                        nonNull(violation.getMessage()) ? violation.getMessage() : "invalid value",
                    (a, b) -> a + ", " + b)));
    return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<ErrorResponse> onWebExchangeBindException(WebExchangeBindException ex) {
    final ErrorResponse errorResponse = new ErrorResponse("Invalid data", HttpStatus.BAD_REQUEST);
    errorResponse.setErrors(
        ex.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.toUnmodifiableMap(
                    FieldError::getField,
                    fe ->
                        nonNull(fe.getDefaultMessage()) ? fe.getDefaultMessage() : "invalid value",
                    (a, b) -> a + ", " + b)));
    return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> onException(Exception ex) {
    final ErrorResponse errorResponse =
        new ErrorResponse("Oops! Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
  }

  @Getter
  @Setter
  @Accessors(chain = true)
  @ToString
  public static class ErrorResponse {
    private String message;
    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private Map<String, String> errors = Map.of();

    public ErrorResponse(String message) {
      this.message = message;
    }

    public ErrorResponse(String message, HttpStatus status) {
      this.message = message;
      this.status = status;
    }
  }
}
