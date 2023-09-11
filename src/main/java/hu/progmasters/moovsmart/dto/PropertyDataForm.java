package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PropertyDataForm {

    private PropertyCondition condition;
    @Min(value = 1700, message = "Year of building can not be earlier than 1700")
    @Max(value = 2023, message = "Year of building can not be in the future")
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
