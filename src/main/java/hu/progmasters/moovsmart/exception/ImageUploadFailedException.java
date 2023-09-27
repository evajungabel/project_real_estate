package hu.progmasters.moovsmart.exception;

public class ImageUploadFailedException extends  RuntimeException{

    private final String username;
    private final Long propertyId;


    public ImageUploadFailedException(String username, Long propertyId) {
        super("Image uploading fail with username: " + username + ", and propertyId: " + propertyId);
        this.username = username;
        this.propertyId = propertyId;
    }
}
