package hu.progmasters.moovsmart.exception;

public class ImageDeleteFailedException extends RuntimeException{

    private final String username;
    private final Long id;


    public ImageDeleteFailedException(String username, Long id) {
        super("Image deleting fail with username: " + username + ", and id: " + id);
        this.username = username;
        this.id = id;
    }

}
