package hu.progmasters.moovsmart;

import hu.progmasters.moovsmart.service.EmailService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
public class MoovsmartApplication {

//	@Autowired
//	private EmailService emailService;
	public static void main(String[] args) {
		SpringApplication.run(MoovsmartApplication.class, args);
	}


//	@EventListener(ApplicationReadyEvent.class)
//	public void sendMail(){
//		emailService.sendEmail("eva.jungabel@gmail.com", "Subject", "Body");
//	}

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
						.title("Estate trade API")
						.version("1.0.1")
						.description("This is a Estate trade application."));
	}



//	@Bean
//	public JavaMailSender getJavaMailSender() {
//		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//		mailSender.setHost("smtp.gmail.com");
//		mailSender.setPort(587);
//
//		mailSender.setUsername("eva.jungabel@gmail.com");
//		mailSender.setPassword("password");
//
//		Properties props = mailSender.getJavaMailProperties();
//		props.put("mail.transport.protocol", "smtp");
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.debug", "true");
//
//		return mailSender;
//	}

}
