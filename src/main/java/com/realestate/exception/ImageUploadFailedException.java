package com.realestate.exception;

public class ImageUploadFailedException extends  RuntimeException{

    private final String username;
    private final Long id;


    public ImageUploadFailedException(String username, Long id) {
        super("Image uploading fail with username: " + username + ", and id: " + id);
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }
}
