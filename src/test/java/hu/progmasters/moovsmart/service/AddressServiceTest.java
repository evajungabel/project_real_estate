package hu.progmasters.moovsmart.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AddressServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_saveAddress() throws Exception {

        String inputCommand = "{\n" +
                "    \"zipcode\": 2200,\n" +
                "    \"country\": \"Magyarország\",\n" +
                "    \"city\": \"Bénye\",\n" +
                "    \"street\": \"Alkotmány utca\",\n" +
                "    \"houseNumber\": \"4/A\",\n" +
                "    \"doorNumber\": 1,\n" +
                "    \"propertyId\": 1\n" +
                "}";

        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isCreated());
    }
}