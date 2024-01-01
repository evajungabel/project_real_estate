package com.realestate.exception;

public class TokenCannotBeUsedException extends RuntimeException {

    private final String tokenId;

    public TokenCannotBeUsedException(String tokenId) {
        this.tokenId = tokenId;
    }
}
