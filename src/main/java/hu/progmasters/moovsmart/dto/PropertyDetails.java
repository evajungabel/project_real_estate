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


    public PropertyDetails(Property property) {
        this.id = property.getId();
        this.name = property.getName();
        this.numberOfRooms = property.getNumberOfRooms();
        this.price = property.getPrice();
        this.description = property.getDescription();
        this.imageUrl = property.getImageUrl();
    }

}
