package com.airbnb.common.exceptions.invalid;

public class InvalidException extends RuntimeException{
    public InvalidException() {
    }

    public InvalidException(String message) {
        super(message);
    }
}
