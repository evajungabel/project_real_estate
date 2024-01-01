package com.realestate.dto;

import com.realestate.dto.weather.WeatherData;
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
