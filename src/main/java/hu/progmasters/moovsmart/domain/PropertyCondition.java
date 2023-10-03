package hu.progmasters.moovsmart.domain;

public enum PropertyCondition {
    NEWLY_BUILT("újépítésü"),
    LIKE_NEW("újszerü"),
    RENOVATED("felújított"),
    IN_GOOD_CONDITION("jó állapotú"),
    IN_MEDIUM_CONDITION("közepes állapotú"),
    NEEDS_RENOVATION("felújítandó"),
    INCOMPLETE("szerkezetkész");

    private final String value;

    PropertyCondition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
