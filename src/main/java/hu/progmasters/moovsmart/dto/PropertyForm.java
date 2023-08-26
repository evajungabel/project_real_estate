package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PropertyForm {


    @NotBlank(message = "Property name cannot be empty!")
    @Size(min = 1, max = 200, message = "Property name must be between 1 and 200 characters!")
    private String name;

    @NotNull(message = "Property type cannot be empty!")
    @Size(min = 1, max = 200, message = "Property type must be between 1 and 200 characters!")
    private PropertyType type;

    @Min(value = 1, message = "Space must be between 1 and 1000!")
    @Max(value = 12, message = "Space must be between 1 and 1000!")
    private String space;


    @Min(value = 1, message = "Number of rooms must be between 1 and 12!")
    @Max(value = 12, message = "Number of rooms must be between 1 and 12!")
    private int numberOfRooms;

    private int price;
    private String description;
    private String imageUrl;


}
