package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PropertyDataInfo {

    private PropertyCondition propertyCondition;

    private Integer yearBuilt;

    private PropertyParking propertyParking;

    private PropertyOrientation propertyOrientation;

    private PropertyHeatingType propertyHeatingType;

    private PropertyEnergyPerformanceCertificate energyCertificate;

    private Boolean hasBalcony;

    private Boolean hasLift;

    private Boolean isAccessible;

    private Boolean isInsulated;

    private Boolean hasAirCondition;

    private Boolean hasGarden;
}
