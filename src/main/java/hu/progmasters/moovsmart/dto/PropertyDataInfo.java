package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PropertyDataInfo {

    private PropertyCondition condition;

    private int yearBuilt;

    private PropertyParking parking;

    private PropertyOrientation orientation;

    private PropertyHeatingType heating;

    private PropertyEnergyPerformanceCertificate energyCertificate;

    private boolean hasBalcony;

    private boolean hasLift;

    private boolean isAccessible;

    private boolean isInsulated;

    private boolean hasAirCondition;

    private boolean hasGarden;
}
