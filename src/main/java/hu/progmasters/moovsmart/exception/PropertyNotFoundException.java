package hu.progmasters.moovsmart.exception;

public class PropertyNotFoundException extends RuntimeException{

    private Long propertyId;

    public PropertyNotFoundException(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getPropertyId() {
        return propertyId;
    }
}
