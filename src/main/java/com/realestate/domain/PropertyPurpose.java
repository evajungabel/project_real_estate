package com.realestate.domain;

public enum PropertyPurpose {
    FOR_SALE("ELADÓ"),
    TO_RENT("KIADÓ");

   private final String value;

    PropertyPurpose(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
