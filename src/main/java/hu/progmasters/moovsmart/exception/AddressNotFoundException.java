package hu.progmasters.moovsmart.exception;

import lombok.Getter;

@Getter
public class AddressNotFoundException extends RuntimeException {
    private final Long addressId;

    public AddressNotFoundException(Long addressId) {
        super("No player found with id: " + addressId);
        this.addressId = addressId;
    }
}

