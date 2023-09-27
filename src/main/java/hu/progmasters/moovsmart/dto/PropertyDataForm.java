package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.*;
import hu.progmasters.moovsmart.validation.NotInFuture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

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
