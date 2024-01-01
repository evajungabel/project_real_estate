package com.realestate.exception;

public class NoResourceFoundException  extends RuntimeException {

    private final int totalPages;

    public NoResourceFoundException(Integer totalPages) {
        super("No resource found with size: " + totalPages);
        this.totalPages = totalPages;
    }

}
