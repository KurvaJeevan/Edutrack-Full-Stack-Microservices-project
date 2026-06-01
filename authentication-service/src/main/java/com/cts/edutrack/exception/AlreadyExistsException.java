package com.cts.edutrack.exception;
 
public class AlreadyExistsException extends RuntimeException {
 
    public AlreadyExistsException(String message) {
        super(message);
    }
 
    public AlreadyExistsException() {
    }
}