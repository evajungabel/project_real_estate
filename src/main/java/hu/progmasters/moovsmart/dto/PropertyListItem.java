package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.Property;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PropertyListItem {

    private long id;
    private String name;
    private int numberOfRooms;
    private int price;
    private String imageUrl;

}
