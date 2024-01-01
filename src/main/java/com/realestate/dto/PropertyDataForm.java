package com.realestate.dto;

import com.realestate.domain.*;
import com.realestate.domain.*;
import com.realestate.validation.NotInFuture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PropertyDataForm {

    private PropertyCondition propertyCondition;

    @NotInFuture(message = "Year of building can not be in the future")
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
