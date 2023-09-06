package hu.progmasters.moovsmart.exception;


public class UsernameExistsException extends RuntimeException {

    private final String username;

    public UsernameExistsException(String username) {
        this.username = username;
    }
}
