package com.arnor4eck.util.exception;

public class DataBaseNotInitializedException extends RuntimeException {
    public DataBaseNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }
}
