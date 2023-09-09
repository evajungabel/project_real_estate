package hu.progmasters.moovsmart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeatherApiConfig {
    @Value("${spring.openweathermap.api.key}")
    private String openWeatherMapApiKey;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getOpenWeatherMapApiKey() {
        return openWeatherMapApiKey;
    }
}
