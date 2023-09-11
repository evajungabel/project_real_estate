package hu.progmasters.moovsmart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PropertyInfo {
    private long id;
    private String name;
    private int numberOfRooms;
    private int price;
    private String imageUrl;
}
