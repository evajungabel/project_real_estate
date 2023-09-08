package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.WeatherApiConfig;
import hu.progmasters.moovsmart.dto.weather.Coordinate;
import hu.progmasters.moovsmart.dto.weather.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private final WeatherApiConfig appConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public WeatherService(WeatherApiConfig appConfig, RestTemplate restTemplate) {
        this.appConfig = appConfig;
        this.restTemplate = restTemplate;
    }

    public WeatherData getWeatherForCoordinates(Double lat, Double lon) {
        String apiKey = appConfig.getOpenWeatherMapApiKey();
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" +
                lat + "&lon=" +
                lon + "&appid=" + apiKey;

        ResponseEntity<WeatherData> response = restTemplate.getForEntity(apiUrl, WeatherData.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }


    public Coordinate getCoordinatesForZip(String zipcode) {
        String apiKey = appConfig.getOpenWeatherMapApiKey();
        String apiUrl = "http://api.openweathermap.org/geo/1.0/zip?zip=" + zipcode + ",HU&appid=" + apiKey;

        ResponseEntity<Coordinate> response = restTemplate.getForEntity(apiUrl, Coordinate.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }
}
