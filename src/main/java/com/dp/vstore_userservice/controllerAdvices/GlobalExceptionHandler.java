package com.dp.vstore_userservice.controllerAdvices;

import com.dp.vstore_userservice.exceptions.UserAlreadyPresentException;
import com.dp.vstore_userservice.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<Map<String, String>> exception(String message, HttpStatus status) {
        Map<String, String> map = new HashMap<>();
        map.put("error", message);
        return new ResponseEntity<>(map, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Map<String, String>>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Map<String, String>> map = new HashMap<>();
        Map<String, String> value = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((error) -> {
            value.put(error.getField(), error.getDefaultMessage());
        });
        map.put("error", value);
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyPresentException.class)
    public ResponseEntity<Map<String, String>> userAlreadyPresentException(UserAlreadyPresentException e) {
        return exception(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> userNotFoundException(UserNotFoundException e) {
        return exception(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> exception(Exception e) {
        return exception(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
