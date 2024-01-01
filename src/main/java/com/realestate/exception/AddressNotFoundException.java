package com.realestate.exception;

import lombok.Getter;

@Getter
public class AddressNotFoundException extends RuntimeException {
    private final Long addressId;

    public AddressNotFoundException(Long addressId) {
        super("No address found with id: " + addressId);
        this.addressId = addressId;
    }
}

