package hu.progmasters.moovsmart.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainData {
    private Double temp;
    private Integer humidity;
}
