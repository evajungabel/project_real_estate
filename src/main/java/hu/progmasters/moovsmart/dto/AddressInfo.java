package hu.progmasters.moovsmart.dto;


import hu.progmasters.moovsmart.dto.weather.WeatherData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressInfo {

    private Integer zipcode;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private Integer doorNumber;
    private String propertyName;
    private WeatherData weatherData;
}
