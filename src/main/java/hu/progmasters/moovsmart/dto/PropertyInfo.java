package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.PropertyImageURL;
import hu.progmasters.moovsmart.domain.PropertyPurpose;
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

    private PropertyPurpose purpose;
    private Integer numberOfRooms;
    private Integer price;
    private List<PropertyImageURL> propertyImageURLS;
}
