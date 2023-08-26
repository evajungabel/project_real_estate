package hu.progmasters.moovsmart.config;

public enum CustomUserRole {

    ROLE_GUEST("GUEST"),
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private final String role;

    CustomUserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
