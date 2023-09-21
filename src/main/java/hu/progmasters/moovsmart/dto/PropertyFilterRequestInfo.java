package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PropertyFilterRequestInfo {

    private String name;
    private PropertyType type;
    private PropertyPurpose purpose;
    private Double area;
    private Integer numberOfRooms;
    private Double price;
    private AddressInfoForProperty addressInfoForProperty;
    private PropertyDataInfo propertyDataInfo;
}
