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
public class PropertyRequests {
    private PropertyType type;
    private PropertyPurpose purpose;
    private Integer area;
    private Integer numberOfRooms;
    private Integer price;
    private String description;
    private PropertyStatus status;
    private PropertyData propertyData;
    private Address address;

}
