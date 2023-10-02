package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.dto.weather.WeatherData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressInfoWeather {
    private Integer zipcode;
    private String country;
    private String city;
    private WeatherData weatherData;
}
