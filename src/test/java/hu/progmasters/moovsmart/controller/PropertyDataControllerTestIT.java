package hu.progmasters.moovsmart.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class PropertyDataControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;


    @Test
    void IT_test_atStart_emptyList() throws Exception {
        mockMvc.perform(get("/api/properties/data/3"))
                .andExpect(status().isOk());
    }

    @Test
    void IT_test_findAllPropertiesPaginatedAndSorted() throws Exception {
        mockMvc.perform(get("/api/properties/data/paginatedandsortedlist?page=0&size=15&sort=yearBuilt&sortDir=ASC")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].yearBuilt", is(1987)))
                .andExpect(jsonPath("$[1].yearBuilt", is(1999)))
                .andExpect(jsonPath("$[2].yearBuilt", is(2003)));
    }




    @Test
    void IT_test_getPropertyDataDetailsForPropertyId() throws Exception {
        mockMvc.perform(get("/api/properties/data/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.energyCertificate", is("AT_LEAST_AA_PLUS_PLUS")))
                .andExpect(jsonPath("$.hasAirCondition", is(true)))
                .andExpect(jsonPath("$.hasBalcony", is(true)))
                .andExpect(jsonPath("$.hasGarden", is(true)))
                .andExpect(jsonPath("$.hasLift", is(false)))
                .andExpect(jsonPath("$.isAccessible", is(true)))
                .andExpect(jsonPath("$.isInsulated", is(true)))
                .andExpect(jsonPath("$.propertyHeatingType", is("GAS_CONVECTOR")))
                .andExpect(jsonPath("$.propertyOrientation", is("NORTH_WEST")))
                .andExpect(jsonPath("$.propertyParking", is("COURTYARD")))
                .andExpect(jsonPath("$.yearBuilt", is(1987)));
    }


    @Test
    void IT_test_getPropertyDataDetailsWithNoId() throws Exception {
        mockMvc.perform(get("/api/properties/data/16")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Property not found error.")))
                .andExpect(jsonPath("$.details", is("No property found with id: 16")));
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_savePropertyDataForPropertyId() throws Exception {

        String inputCommand = "{\n" +
                "    \"energyCertificate\": \"AT_LEAST_AA\",\n" +
                "    \"hasAirCondition\": \"true\",\n" +
                "    \"hasBalcony\": \"true\",\n" +
                "    \"hasGarden\": \"true\",\n" +
                "    \"hasLift\": \"true\",\n" +
                "    \"isAccessible\": \"true\",\n" +
                "    \"isInsulated\": \"true\",\n" +
                "    \"propertyHeatingType\": \"CENTRAL_HEATING\",\n" +
                "    \"propertyOrientation\": \"WEST\",\n" +
                "    \"propertyParking\": \"UNDERGROUND_GARAGE_SPACE\",\n" +
                "    \"yearBuilt\": 1971 \n" +
                "}";

        mockMvc.perform(post("/api/properties/data/4")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.energyCertificate", is("AT_LEAST_AA")))
                .andExpect(jsonPath("$.hasAirCondition", is(true)))
                .andExpect(jsonPath("$.hasBalcony", is(true)))
                .andExpect(jsonPath("$.hasGarden", is(true)))
                .andExpect(jsonPath("$.hasLift", is(true)))
                .andExpect(jsonPath("$.isAccessible", is(true)))
                .andExpect(jsonPath("$.isInsulated", is(true)))
                .andExpect(jsonPath("$.propertyHeatingType", is("CENTRAL_HEATING")))
                .andExpect(jsonPath("$.propertyOrientation", is("WEST")))
                .andExpect(jsonPath("$.propertyParking", is("UNDERGROUND_GARAGE_SPACE")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_savePropertyData_yearOfBuiltNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"energyCertificate\": \"AT_LEAST_AA\",\n" +
                "    \"hasAirCondition\": \"true\",\n" +
                "    \"hasBalcony\": \"true\",\n" +
                "    \"hasGarden\": \"true\",\n" +
                "    \"hasLift\": \"true\",\n" +
                "    \"isAccessible\": \"true\",\n" +
                "    \"isInsulated\": \"true\",\n" +
                "    \"propertyHeatingType\": \"CENTRAL_HEATING\",\n" +
                "    \"propertyOrientation\": \"WEST\",\n" +
                "    \"propertyParking\": \"UNDERGROUND_GARAGE_SPACE\",\n" +
                "    \"yearBuilt\": 7800\n" +
                "}";
        mockMvc.perform(post("/api/properties/data/4")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest());
    }



    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_updatePropertyDataWithPropertyId() throws Exception {

        String inputCommand = "{\n" +
                "    \"energyCertificate\": \"AT_LEAST_AA\",\n" +
                "    \"hasAirCondition\": \"true\",\n" +
                "    \"hasBalcony\": \"true\",\n" +
                "    \"hasGarden\": \"true\",\n" +
                "    \"hasLift\": \"true\",\n" +
                "    \"isAccessible\": \"true\",\n" +
                "    \"isInsulated\": \"true\",\n" +
                "    \"propertyHeatingType\": \"CENTRAL_HEATING\",\n" +
                "    \"propertyOrientation\": \"WEST\",\n" +
                "    \"propertyParking\": \"UNDERGROUND_GARAGE_SPACE\",\n" +
                "    \"yearBuilt\": 1963 \n" +
                "}";

        mockMvc.perform(put("/api/properties/data/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.energyCertificate", is("AT_LEAST_AA")))
                .andExpect(jsonPath("$.hasAirCondition", is(true)))
                .andExpect(jsonPath("$.hasBalcony", is(true)))
                .andExpect(jsonPath("$.hasGarden", is(true)))
                .andExpect(jsonPath("$.hasLift", is(true)))
                .andExpect(jsonPath("$.isAccessible", is(true)))
                .andExpect(jsonPath("$.isInsulated", is(true)))
                .andExpect(jsonPath("$.propertyHeatingType", is("CENTRAL_HEATING")))
                .andExpect(jsonPath("$.propertyOrientation", is("WEST")))
                .andExpect(jsonPath("$.propertyParking", is("UNDERGROUND_GARAGE_SPACE")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_updatePropertyDataWithPropertyId_yearOfBuiltNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"energyCertificate\": \"AT_LEAST_AA\",\n" +
                "    \"hasAirCondition\": \"true\",\n" +
                "    \"hasBalcony\": \"true\",\n" +
                "    \"hasGarden\": \"true\",\n" +
                "    \"hasLift\": \"true\",\n" +
                "    \"isAccessible\": \"true\",\n" +
                "    \"isInsulated\": \"true\",\n" +
                "    \"propertyHeatingType\": \"CENTRAL_HEATING\",\n" +
                "    \"propertyOrientation\": \"WEST\",\n" +
                "    \"propertyParking\": \"UNDERGROUND_GARAGE_SPACE\",\n" +
                "    \"yearBuilt\": 7800\n" +
                "}";

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest());
    }


}
