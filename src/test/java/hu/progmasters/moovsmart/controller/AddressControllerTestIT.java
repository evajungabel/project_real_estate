package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.domain.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class AddressControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    void test_atStart_emptyList() throws Exception {
        mockMvc.perform(get("/api/addresses"))
                .andExpect(status().isOk());
    }


    @Test
    void IT_saveAddress_test() throws Exception {

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
    void IT_addressFindById_test() throws Exception {

        mockMvc.perform(get("/api/addresses/id/4")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city", is("Tök")))
                .andExpect(jsonPath("$.country", is("Magyarország")))
                .andExpect(jsonPath("$.houseNumber", is("2")))
                .andExpect(jsonPath("$.zipcode", is(2202)))
                .andExpect(jsonPath("$.propertyName", is("Eladó lakás Pécsett")));
    }

    @Test
    void IT_weatherFindById_test() throws Exception {

        mockMvc.perform(get("/api/addresses/weather/id/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city", is("Monor")))
                .andExpect(jsonPath("$.country", is("Magyarország")))
                .andExpect(jsonPath("$.zipcode", is(2200)));
    }

    @Test
    void IT_weather_NOT_Find_test() throws Exception {

        mockMvc.perform(get("/api/addresses/weather/id/14")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("14"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", is("No weather information found with id: 14")));
    }

    @Test
    void IT_addressUpdate_test()  throws Exception {
        String input = "{" +
                "\"zipcode\": 2222," +
                "\"country\": \"Magyarország\"," +
                "\"city\": \"Tök\"," +
                "\"street\": \"Almafa utca\"," +
                "\"houseNumber\": \"43/z\"," +
                "\"doorNumber\": 14," +
                "\"propertyId\": 3 " +
                "}";
        mockMvc.perform(put("/api/addresses/id/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zipcode", is(2222)))
                .andExpect(jsonPath("$.city", is("Tök")))
                .andExpect(jsonPath("$.street", is("Almafa utca")));
    }

    @Test
    void IT_deleteAddress_test() throws Exception {
        Address address = entityManager.find(Address.class, 1L);
        assertFalse(address.getDeleted());
        mockMvc.perform(delete("/api/addresses/id/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertTrue(address.getDeleted());
    }

    @Test
    void IT_AddressNotExists_test() throws Exception {
        mockMvc.perform(delete("/api/addresses/id/34")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", is("No address found with id: 34")));
    }


    @Test
    void IT_addressFindByValue_test() throws Exception {
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
