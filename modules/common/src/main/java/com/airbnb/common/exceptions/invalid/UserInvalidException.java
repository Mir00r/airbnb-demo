package com.airbnb.common.exceptions.invalid;

public class UserInvalidException extends InvalidException {
    public UserInvalidException() {
    }

    public UserInvalidException(String message) {
        super(message);
    }
}
