package com.airbnb.common.exceptions.unknown;


import com.airbnb.common.exceptions.forbidden.ForbiddenException;

public class WtfException extends ForbiddenException {
    public WtfException() {
    }

    public WtfException(String message) {
        super(message);
    }
}
