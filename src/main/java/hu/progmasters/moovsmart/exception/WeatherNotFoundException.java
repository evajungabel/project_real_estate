package hu.progmasters.moovsmart.exception;

import lombok.Getter;

@Getter
public class WeatherNotFoundException extends RuntimeException {
    private final Long addressId;

    public WeatherNotFoundException(Long addressId) {
        super("No weather information found with id: " + addressId);
        this.addressId = addressId;
    }
}
