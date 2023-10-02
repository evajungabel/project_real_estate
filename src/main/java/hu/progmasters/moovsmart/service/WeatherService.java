package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.ProjectConfig;
import hu.progmasters.moovsmart.dto.weather.Coordinate;
import hu.progmasters.moovsmart.dto.weather.WeatherData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WeatherService {
    private final ProjectConfig appConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public WeatherService(ProjectConfig appConfig, RestTemplate restTemplate) {
        this.appConfig = appConfig;
        this.restTemplate = restTemplate;
    }

    public WeatherData getWeatherForCoordinates(Double lat, Double lon) {
        String apiKey = appConfig.getOpenWeatherMapApiKey();
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" +
                lat + "&lon=" +
                lon + "&appid=" + apiKey;

        try {
            log.info("RestTemplate GET WeatherData");
            ResponseEntity<WeatherData> response = restTemplate.getForEntity(apiUrl, WeatherData.class);
            log.info("RestTemplate response OK");

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                log.error("Weather API did not return a 2xx HTTP status code: " + response.getStatusCodeValue());
                return null;
            }
        } catch (RestClientException e) {
            log.error("An error occurred during the Weather API call: " + e.getMessage(), e);
            return null;
        }
    }

    public Coordinate getCoordinatesForZip(String zipcode) {
        String apiKey = appConfig.getOpenWeatherMapApiKey();
        String apiUrl = "http://api.openweathermap.org/geo/1.0/zip?zip=" + zipcode + ",HU&appid=" + apiKey;

        try {
            log.info("RestTemplate GET coordinate");
            ResponseEntity<Coordinate> response = restTemplate.getForEntity(apiUrl, Coordinate.class);
            log.info("RestTemplate response OK");

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                log.error("Geo API did not return a 2xx HTTP status code: " + response.getStatusCodeValue());
                return null;
            }
        } catch (RestClientException e) {
            log.error("An error occurred during the Geo API call: " + e.getMessage(), e);
            return null;
        }
    }
}
