package com.example.demo.controller;

import com.example.demo.exception.ApplicationNotFoundException;
import com.example.demo.exception.InvalidApiKeyException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidApiKeyException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleInvalidApiKey(InvalidApiKeyException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "https://tools.ietf.org/html/rfc9457#section-3.1");
        body.put("title", "Unauthorized");
        body.put("status", 401);
        body.put("detail", ex.getMessage());
        body.put("instance", "/logs");
        body.put("timestamp", LocalDateTime.now().toString());
        return body;
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleAppNotFound(ApplicationNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "https://tools.ietf.org/html/rfc9457#section-3.1");
        body.put("title", "Not Found");
        body.put("status", 404);
        body.put("detail", ex.getMessage());
        body.put("timestamp", LocalDateTime.now().toString());
        return body;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(ConstraintViolationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "https://tools.ietf.org/html/rfc9457#section-3.1");
        body.put("title", "Bad Request");
        body.put("status", 400);
        body.put("detail", ex.getMessage());
        body.put("timestamp", LocalDateTime.now().toString());
        return body;
    }
}
