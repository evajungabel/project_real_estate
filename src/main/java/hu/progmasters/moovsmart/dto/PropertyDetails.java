package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.PropertyPurpose;
import hu.progmasters.moovsmart.domain.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PropertyDetails {
    private String name;
    private Integer numberOfRooms;
    private Integer price;
    private PropertyType type;
    private PropertyPurpose purpose;
    private Integer area;
    private String description;
    private String imageUrl;
}
