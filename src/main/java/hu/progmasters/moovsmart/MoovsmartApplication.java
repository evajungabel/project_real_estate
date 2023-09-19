package hu.progmasters.moovsmart;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Timer;

@SpringBootApplication
public class MoovsmartApplication {
	public static void main(String[] args) {
		SpringApplication.run(MoovsmartApplication.class, args);
	}


}
