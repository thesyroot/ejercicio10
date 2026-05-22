package com.example.demo.controller;

import com.example.demo.exception.InvalidTareaException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTareaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleInvalidTarea(InvalidTareaException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "https://tools.ietf.org/html/rfc9457#section-3.1");
        body.put("title", "Bad Request");
        body.put("status", 400);
        body.put("detail", ex.getMessage());
        body.put("timestamp", LocalDateTime.now().toString());
        return body;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "https://tools.ietf.org/html/rfc9457#section-3.1");
        body.put("title", "Bad Request");
        body.put("status", 400);
        
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Error de validación");
        body.put("detail", errors);
        body.put("timestamp", LocalDateTime.now().toString());
        return body;
    }
}
