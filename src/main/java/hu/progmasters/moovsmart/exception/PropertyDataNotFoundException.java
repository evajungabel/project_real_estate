package hu.progmasters.moovsmart.exception;

public class PropertyDataNotFoundException extends RuntimeException {

    private final Long propertyId;

    public PropertyDataNotFoundException(Long propertyId) {
        super("No property data found with property id: " + propertyId);
        this.propertyId = propertyId;
    }

    public Long getPropertyId() {
        return propertyId;
    }
}
