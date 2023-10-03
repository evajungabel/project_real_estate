package hu.progmasters.moovsmart.exception;

public class CustomUserHasMoreInputsForTheGameException  extends RuntimeException {
    private final String username;
    public CustomUserHasMoreInputsForTheGameException(String username) {
        super("CustomUser has more inputs for the game with username: " + username);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
