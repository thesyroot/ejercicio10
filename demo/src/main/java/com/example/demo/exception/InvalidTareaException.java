package com.example.demo.exception;

public class InvalidTareaException extends RuntimeException {
    public InvalidTareaException(String message) {
        super(message);
    }
}
