package com.airbnb.common.exceptions.notfound;

public class FirebaseTokenNotFoundException extends NotFoundException {
    public FirebaseTokenNotFoundException() {
        super();
    }

    public FirebaseTokenNotFoundException(String s) {
        super(s);
    }
}
