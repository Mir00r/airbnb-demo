package com.airbnb.common.exceptions.limitExceed;


import com.airbnb.common.exceptions.invalid.InvalidException;

public class LimitExceedException extends InvalidException {
    public LimitExceedException() {
    }

    public LimitExceedException(String message) {
        super(message);
    }

}
