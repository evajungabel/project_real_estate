package hu.progmasters.moovsmart.exception;

public class PropertyImageURLNotFoundException extends RuntimeException {

    private final Long propertyImageURLId;

    public PropertyImageURLNotFoundException(Long propertyImageURLId) {
        super("PropertyImageURL was not found with id: " + propertyImageURLId);
        this.propertyImageURLId = propertyImageURLId;
    }

    public Long getPropertyImageURLId() {
        return propertyImageURLId;
    }
}
