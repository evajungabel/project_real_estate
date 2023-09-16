package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.PropertyImageURL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PropertyImageURLForm {

    private String propertyImageURL;
}
