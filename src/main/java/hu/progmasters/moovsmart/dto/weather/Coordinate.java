package hu.progmasters.moovsmart.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {
    private String zip;
    private String name;
    private Double lat;
    private Double lon;
    private String country;
}
