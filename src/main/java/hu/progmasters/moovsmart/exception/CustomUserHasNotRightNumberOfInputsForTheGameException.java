package hu.progmasters.moovsmart.exception;

public class CustomUserHasNotRightNumberOfInputsForTheGameException extends RuntimeException {
    private final String username;
    public CustomUserHasNotRightNumberOfInputsForTheGameException(String username) {
        super("CustomUser has not right number of inputs for the game with username: " + username);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
