package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.PropertyImageURL;
import hu.progmasters.moovsmart.domain.PropertyPurpose;
import hu.progmasters.moovsmart.domain.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PropertyInfo {

    private Long id;
    private String name;
    private PropertyType type;
    private PropertyPurpose purpose;
    private Double area;
    private Integer numberOfRooms;
    private Double price;
    private AddressInfoForProperty addressInfoForProperty;
    private List<PropertyImageURL> propertyImageURLS;
    private PropertyDataInfo propertyDataInfo;
}
