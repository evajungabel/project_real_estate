package hu.progmasters.moovsmart.exception;

public class EmailAddressNotFoundException extends RuntimeException {

    private final String email;

    public EmailAddressNotFoundException(String email) {
        super("Email address was not found with: " + email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
