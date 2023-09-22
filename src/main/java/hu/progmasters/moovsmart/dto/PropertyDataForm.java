package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PropertyDataForm {

    private PropertyCondition propertyCondition;
    @Min(value = 1700, message = "Year of building can not be earlier than 1700")
    @Future(message = "Year of building can not be in the future")
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
