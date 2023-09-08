package hu.progmasters.moovsmart.dto.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hu.progmasters.moovsmart.dto.weather.Weather;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherData {
    private List<Weather> weather;
    private MainData main;

    public Double getTemperatureInCelsius() {
        if (main != null && main.getTemp() != null) {
            return main.getTemp() - 273.15;
        }
        return null;
    }
}
