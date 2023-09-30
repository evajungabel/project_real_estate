package hu.progmasters.moovsmart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeApiConfig {
    @Value("${spring.exchangerates.api.key}")
    private String exchangeratesApiKey;


    public String getExchangeratesApiKey() {
        return exchangeratesApiKey;
    }
}
