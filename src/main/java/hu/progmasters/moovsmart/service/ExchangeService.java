package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.ProjectConfig;
import hu.progmasters.moovsmart.dto.ExchangeData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ExchangeService {
    private final ProjectConfig exchangeApiConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public ExchangeService(ProjectConfig exchangeApiConfig, RestTemplate restTemplate) {
        this.exchangeApiConfig = exchangeApiConfig;
        this.restTemplate = restTemplate;
    }

    public ExchangeData getexchangeData(String currency) {
        String apiKey = exchangeApiConfig.getExchangeratesApiKey();
        String apiUrl = "http://api.exchangeratesapi.io/v1/latest?access_key=" + apiKey + "&base=EUR&symbols=" + currency;

        try {
            log.info("RestTemplate GET ExchangeData");
            ResponseEntity<ExchangeData> response = restTemplate.getForEntity(apiUrl, ExchangeData.class);
            log.info("RestTemplate response OK");

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                log.error("Weather API did not return a 2xx HTTP status code: " + response.getStatusCodeValue());
                return null;
            }
        } catch (RestClientException e) {
            log.error("An error occurred during the Exchange API call: " + e.getMessage(), e);
            return null;
        }
    }

    public String changePrice(Double price, String currency) {
        ExchangeData exchangeData1 = getexchangeData("HUF");
        Double pEuro = (exchangeData1.getRates().get("HUF")) / price;
        ExchangeData exchangeData2 = getexchangeData(currency);
        Double result = (exchangeData2.getRates().get(currency)) / pEuro;

        String formPrice = String.format("%.2f", price);
        String formResult = String.format("%.2f", result);

        return "Átváltás: \n" +
                "HUF: " + formPrice + "\n" +
                currency + ": " + formResult;
    }
}
