package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.service.EmailService;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class SpringMailIntegrationTest {

    @Autowired
    private EmailService emailService;


    @Mock
    @Rule
    public SmtpServerRule smtpServerRule = new SmtpServerRule(465);

    @Test
    public void shouldSendSingleMail() throws MessagingException, IOException {
        SimpleMailMessage mail = new SimpleMailMessage();
        String from = "no-reply@memorynotfound.com";
        String to = "info@memorynotfound.com";
        String subject = "Spring Mail Integration Testing with JUnit and GreenMail Example";
        String text = "Testing";

        emailService.sendEmail(to, subject, text);

        MimeMessage[] receivedMessages = smtpServerRule.getMessages();
        assertEquals(1, receivedMessages.length);

        MimeMessage current = receivedMessages[0];

        assertEquals(mail.getSubject(), current.getSubject());
//        assertEquals(mail.getTo(), current.getAllRecipients()[0].toString());

    }

}