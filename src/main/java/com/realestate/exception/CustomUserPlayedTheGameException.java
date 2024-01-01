package com.realestate.exception;

public class CustomUserPlayedTheGameException extends RuntimeException {

    private final String username;

    public CustomUserPlayedTheGameException(String username) {
        super("CustomUser was played the game today with username: " + username);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
