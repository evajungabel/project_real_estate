package com.realestate.domain;

public enum PropertyType {
    FLAT("lakás"),
    HOUSE("ház"),
    PLOT("telek"),
    VILLA("villa"),
    OFFICE_BUILDING("iroda"),
    DEPOT("raktár"),
    PREDIAL("mezőgazdasági ingatlan"),
    MARKET("üzlethelység"),
    GARAGE("garázs");

    private final String value;

    PropertyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
