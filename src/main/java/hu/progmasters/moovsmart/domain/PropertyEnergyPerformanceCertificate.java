package hu.progmasters.moovsmart.domain;

public enum PropertyEnergyPerformanceCertificate {

    AT_LEAST_AA_PLUS_PLUS("AA++"),
    AT_LEAST_AA_PLUS("AA+"),
    AT_LEAST_AA("AA"),
    AT_LEAST_BB("BB"),
    AT_LEAST_CC("CC"),
    AT_LEAST_DD("DD"),
    AT_LEAST_EE("EE"),
    AT_LEAST_FF("FF"),
    AT_LEAST_GG("GG"),
    AT_LEAST_HH("HH"),
    AT_LEAST_II("II"),
    AT_LEAST_JJ("JJ");

    private final String value;

    PropertyEnergyPerformanceCertificate(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
