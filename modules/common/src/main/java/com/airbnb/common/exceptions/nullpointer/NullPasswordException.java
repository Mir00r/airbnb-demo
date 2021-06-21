package com.airbnb.common.exceptions.nullpointer;

public class NullPasswordException extends NullException {
    public NullPasswordException() {
        super();
    }

    public NullPasswordException(String message) {
        super(message);
    }
}
