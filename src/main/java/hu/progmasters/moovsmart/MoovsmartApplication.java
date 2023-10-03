package hu.progmasters.moovsmart;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoovsmartApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoovsmartApplication.class, args);
    }
}
