package hu.progmasters.moovsmart.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class AddressControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_atStart_emptyList() throws Exception {
        mockMvc.perform(get("/api/addresses"))
                .andExpect(status().isOk());
    }


    @Test
    void IT_saveAddress_test()  throws Exception {

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

    @Test
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findByValue() throws Exception {
        String value = "mon";

        mockMvc.perform(get("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(value))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city", is("Monor")));
    }

    @Test
    void findAllAddress() throws Exception {
        mockMvc.perform(get("/api/addresses")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city", is("Monor")))
                .andExpect(jsonPath("$[1].city", is("Bénye")))
                .andExpect(jsonPath("$[2].city", is("Káva")))
                .andExpect(jsonPath("$[3].city", is("Tök")))
                .andExpect(jsonPath("$[4].city", is("Zsámbék")))
                .andExpect(jsonPath("$[5].city", is("Alma")))
                .andExpect(jsonPath("$[6].city", is("Novigrád")))
                .andExpect(jsonPath("$[7].city", is("Velen")))
                .andExpect(jsonPath("$[8].city", is("Tatuin")))
                .andExpect(jsonPath("$[9].city", is("Pánd")))
                .andExpect(jsonPath("$[10].city", is("Pilis")))
                .andExpect(jsonPath("$[11].city", is("Úri")))
                .andExpect(jsonPath("$[12].city", is("Felcsút")))
                .andExpect(jsonPath("$[13].city", is("Alcsút-Dobozos")))
                .andExpect(jsonPath("$[14].city", is("Péteri")));
    }
}
