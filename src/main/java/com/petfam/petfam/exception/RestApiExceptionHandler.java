package com.petfam.petfam.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

  @ExceptionHandler(value = {IllegalArgumentException.class})
  public ResponseEntity<Object> handleApiRequestException(IllegalArgumentException e) {
    RestApiException restApiException = new RestApiException(e.getMessage(),
        HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(
        restApiException,
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  public ResponseEntity<Object> handleApiRequestException(MethodArgumentNotValidException e) {
    RestApiException restApiException = new RestApiException(
        e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
        HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(
        restApiException,
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(value = {NullPointerException.class})
  public ResponseEntity<Object> handleApiRequestException(NullPointerException e) {
    RestApiException restApiException = new RestApiException(e.getMessage(),
        HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(
        restApiException,
        HttpStatus.BAD_REQUEST
    );
  }

}
