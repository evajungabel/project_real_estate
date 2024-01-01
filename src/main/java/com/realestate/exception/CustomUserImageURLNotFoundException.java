package com.realestate.exception;

public class CustomUserImageURLNotFoundException extends RuntimeException {

    private final Long customUserImageURLId;

    public CustomUserImageURLNotFoundException(Long customUserImageURLId) {
        super("CustomUserImageURL was not found with id: " + customUserImageURLId);
        this.customUserImageURLId = customUserImageURLId;
    }

    public Long getCustomUserImageURLId() {
        return customUserImageURLId;
    }
}
