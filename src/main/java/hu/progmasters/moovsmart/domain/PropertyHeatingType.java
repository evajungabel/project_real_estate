package hu.progmasters.moovsmart.domain;

public enum PropertyHeatingType {
    GAS_CONVECTOR("gáz konvektor") ,
    CENTRAL_HEATING("központi fütés"),
    DISTRICT_HEATING("távfütés"),
    ELECTRIC_BOILER("elektromos bojler"),
    HEARTH("melegítő"),
    GAS_FURNACE("gáz kazán"),
    DUAL_FUEL_FURNACE("vegyestüzelésü kazán"),
    UNDERFLOOR_HEATING("padlófütés"),
    WALL_HEATING("falfütés"),
    CEILING_COOLING_HEATING("klíma"),
    HEAT_PUMP("hőszivattyú"),
    OTHER("egyéb");

    private final String value;

    PropertyHeatingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
