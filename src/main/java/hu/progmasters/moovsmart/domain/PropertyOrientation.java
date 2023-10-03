package hu.progmasters.moovsmart.domain;

public enum PropertyOrientation {
    WEST("nyugat"),
    SOUTH("dél"),
    NORTH("észak"),
    EAST("kelet"),
    NORTH_WEST("észak-nyugat"),
    NORTH_EAST("észak-kelet"),
    SOUTH_WEST("dél-nyugat"),
    SOUTH_EAST("dél-kelet");

    private final String value;

    PropertyOrientation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
