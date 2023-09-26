package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.PropertyPurpose;
import hu.progmasters.moovsmart.domain.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PropertyForm {

    @NotBlank(message = "Property name cannot be empty!")
    @Size(min = 1, max = 200, message = "Property name must be between 1 and 200 characters!")
    private String name;

    @NotNull(message = "Property type cannot be empty!")
    private PropertyType type;

    @NotNull(message = "Property purpose cannot be empty!")
    private PropertyPurpose purpose;

    @Min(value = 1, message = "Space must be between 1 and 1000!")
    @Max(value = 1000, message = "Space must be between 1 and 1000!")
    private Double area;

    @Min(value = 1, message = "Number of rooms must be between 1 and 40!")
    @Max(value = 40, message = "Number of rooms must be between 1 and 40!")
    private Integer numberOfRooms;

    @NotNull(message = "Price must be added!")
    private Double price;

    private String description;

}
