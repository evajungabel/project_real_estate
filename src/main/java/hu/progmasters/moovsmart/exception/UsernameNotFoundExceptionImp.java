package hu.progmasters.moovsmart.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsernameNotFoundExceptionImp extends UsernameNotFoundException {

    private String username;
    public UsernameNotFoundExceptionImp(String username) {
        super("Username was not found with: " + username);
        this.username = username;

    }
}
