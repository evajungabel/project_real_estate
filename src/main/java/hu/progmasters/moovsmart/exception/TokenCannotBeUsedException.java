package hu.progmasters.moovsmart.exception;

public class TokenCannotBeUsedException extends RuntimeException {

    private final Long tokenId;

    public TokenCannotBeUsedException(Long tokenId) {
        this.tokenId = tokenId;
    }
}
