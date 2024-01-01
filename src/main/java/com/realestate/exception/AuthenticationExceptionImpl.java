package com.realestate.exception;

import org.apache.tomcat.websocket.AuthenticationException;

public class AuthenticationExceptionImpl extends AuthenticationException {

    private String username;
    public AuthenticationExceptionImpl(String username) {
        super("User was denied with username: " + username);
        this.username = username;
    }
}
