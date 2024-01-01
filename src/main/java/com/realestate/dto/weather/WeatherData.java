package com.realestate.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeatherData {
    private List<Weather> weather;
    private MainData main;
    private Wind wind;

    public Double getTemperatureInCelsius() {
        if (main != null && main.getTemp() != null) {
            return main.getTemp() - 273.15;
        }
        return null;
    }
}
