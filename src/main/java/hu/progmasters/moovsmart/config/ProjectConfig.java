package hu.progmasters.moovsmart.config;

import com.cloudinary.Cloudinary;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProjectConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Estate trade APP")
                        .version("1.0.1")
                        .description("This is the backand of an Estate trade application."));
    }


    @Bean
    public Cloudinary getCloudinary() {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "dvuarfxuu");
        config.put("api_key", "978623321346782");
        config.put("api_secret", "qSu7IhNadONu8C27hVObBJKKuOk");
        config.put("secure", true);
        return new Cloudinary(config);
    }


//    @Value("${paypal.client.id}")
//    private String clientId;
//    @Value("${paypal.client.secret}")
//    private String clientSecret;
//    @Value("${paypal.mode}")
//    private String mode;
//
//    @Bean
//    public Map<String, String> paypalSdkConfig() {
//        Map<String, String> configMap = new HashMap<>();
//        configMap.put("mode", mode);
//        return configMap;
//    }
//
//    @Bean
//    public OAuthTokenCredential oAuthTokenCredential() {
//        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
//    }
//
//    @Bean
//    public APIContext apiContext() throws PayPalRESTException {
//        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
//        context.setConfigurationMap(paypalSdkConfig());
//        return context;
//    }
}
