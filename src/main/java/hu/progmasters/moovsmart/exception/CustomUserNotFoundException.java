package hu.progmasters.moovsmart.exception;

public class CustomUserNotFoundException extends RuntimeException {

    private final Long userId;

    public CustomUserNotFoundException(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
