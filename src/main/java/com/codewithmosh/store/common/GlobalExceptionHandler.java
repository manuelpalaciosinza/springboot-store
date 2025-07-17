package com.codewithmosh.store.common;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationErrors(
            MethodArgumentNotValidException argumentNotValidException){
        var errors = new HashMap<String,String>();
        argumentNotValidException.getBindingResult().getFieldErrors()
                .forEach(error-> errors.put(error.getField(),error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleUnreadableMessage(){
        return ResponseEntity.badRequest().body(
                new ErrorDto("Invalid request body"));
    }
}
