package com.realestate.exception;

public class EmailAddressNotFoundException extends RuntimeException {

    private final String email;

    public EmailAddressNotFoundException(String email) {
        super("CustomUser was not found with email: " + email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
