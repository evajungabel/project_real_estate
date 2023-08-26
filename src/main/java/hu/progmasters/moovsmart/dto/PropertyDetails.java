package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.Property;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PropertyDetails {

    private long id;
    private String name;
    private int numberOfRooms;
    private int price;
    private String description;
    private String imageUrl;



}
