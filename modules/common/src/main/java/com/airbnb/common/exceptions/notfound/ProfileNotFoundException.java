package com.airbnb.common.exceptions.notfound;

public class ProfileNotFoundException extends NotFoundException {
    public ProfileNotFoundException() {
    }

    public ProfileNotFoundException(String s) {
        super(s);
    }
}
