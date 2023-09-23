package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.domain.PropertyStatus;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class PropertyControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;


    @Test
    void IT_test_atStart_emptyList() throws Exception {
        mockMvc.perform(get("/api/properties?page=0&size=1&sort=area&sortDir=asc"))
                .andExpect(status().isOk());
    }

    @Test
    void IT_test_findAllPropertiesPaginatedAndSorted() throws Exception {
        mockMvc.perform(get("/api/properties?page=0&size=15&sort=area&sortDir=ASC")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Kiadó villa")))
                .andExpect(jsonPath("$[1].name", is("Kiadó lakás")))
                .andExpect(jsonPath("$[2].name", is("Kiadó lakás Pécsett")))
                .andExpect(jsonPath("$[3].name", is("Kiadó lakás")))
                .andExpect(jsonPath("$[4].name", is("Eladó lakás")))
                .andExpect(jsonPath("$[5].name", is("Kiadó balatoni ház")))
                .andExpect(jsonPath("$[6].name", is("Eladó ház")))
                .andExpect(jsonPath("$[7].name", is("Kiadó ház")))
                .andExpect(jsonPath("$[8].name", is("Eladó ház")))
                .andExpect(jsonPath("$[9].name", is("Kiadó lakás")))
                .andExpect(jsonPath("$[10].name", is("Kiadó ház")))
                .andExpect(jsonPath("$[11].name", is("Eladó ház")))
                .andExpect(jsonPath("$[12].name", is("Kiadó lakás a városban")))
                .andExpect(jsonPath("$[13].name", is("Kiadó irodaház")))
                .andExpect(jsonPath("$[14].name", is("Kiadó villa")));
    }

    @Test
    void IT_test_findAllPropertiesPaginatedAndSortedAndFiltered() throws Exception {

        String inputCommand = "{\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"minArea\": 120,\n" +
                "    \"minNumberOfRooms\": 4,\n" +
                "    \"maxPrice\": 580000000\n" +
                "}";

        mockMvc.perform(post("/api/properties/requests?page=0&size=15&sort=area&sortDir=ASC")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputCommand))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Kiadó balatoni ház")))
                .andExpect(jsonPath("$[1].name", is("Kiadó ház")))
                .andExpect(jsonPath("$[2].name", is("Kiadó ház")));
    }



    @Test
    void IT_test_getPropertyDetails() throws Exception {
        mockMvc.perform(get("/api/properties/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Kiadó balatoni ház")))
                .andExpect(jsonPath("$.purpose", is("TO_RENT")))
                .andExpect(jsonPath("$.description", is("Kiadó családi ház a Balton partján")))
                .andExpect(jsonPath("$.numberOfRooms", is(4)))
                .andExpect(jsonPath("$.price", is(6.3E7)));
    }


    @Test
    void IT_test_getPropertyDetailsWithNoId() throws Exception {
        mockMvc.perform(get("/api/properties/16")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Property not found error.")))
                .andExpect(jsonPath("$.details", is("No property found with id: 16")));
    }


    @Test
    @WithMockUser(username = "aprandia", authorities = "ROLE_USER")
    void IT_test_saveProperty() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";
        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Eladó Ház")))
                .andExpect(jsonPath("$.numberOfRooms", is(6)))
                .andExpect(jsonPath("$.price", is(7.5E7)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_saveProperty_nameNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"    \",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Property name cannot be empty!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_saveProperty_nameSizeNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Property name must be between 1 and 200 characters!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_saveProperty_purposeNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("purpose")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Property purpose cannot be empty!")));
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_saveProperty_typeNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"area\": 120,\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_saveProperty_areaMinNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 0,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("area")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Space must be between 1 and 1000!")));
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_saveProperty_areaMaxNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 20000,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("area")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Space must be between 1 and 1000!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_saveProperty_numberOfRoomsMinNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 0,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("numberOfRooms")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Number of rooms must be between 1 and 40!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_saveProperty_numberOfRoomsMaxNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 41,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("numberOfRooms")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Number of rooms must be between 1 and 40!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_saveProperty_priceNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("price")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Price must be added!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_saveProperty_customUsernameNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\"\n" +
                "}";

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("User not found error.")))
                .andExpect(jsonPath("$.details", is("Username was not found with: user")));
    }


    @Test
    @WithMockUser(username = "aprandia", authorities = "ROLE_USER")
    void IT_test_updateProperty() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"FLAT\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Csssssss ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";


        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Eladó Ház")))
                .andExpect(jsonPath("$.numberOfRooms", is(6)))
                .andExpect(jsonPath("$.price", is(7.5E7)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_updateProperty_nameNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"    \",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Property name cannot be empty!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_updateProperty_nameSizeNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Property name must be between 1 and 200 characters!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_updateProperty_typeNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("type")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Property type cannot be empty!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_updateProperty_purposeNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("purpose")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Property purpose cannot be empty!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_updateProperty_areaMinNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 0,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("area")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Space must be between 1 and 1000!")));
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_updateProperty_areaMaxNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 20000,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("area")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Space must be between 1 and 1000!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_updateProperty_numberOfRoomsMinNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 0,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("numberOfRooms")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Number of rooms must be between 1 and 40!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_updateProperty_numberOfRoomsMaxNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 41,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("numberOfRooms")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Number of rooms must be between 1 and 40!")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void IT_test_updateProperty_priceNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\",\n" +
                "    \"customUsername\": \"aprandia\"\n" +
                "}";

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("price")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Price must be added!")));
    }

    @Test
    @WithMockUser(username = "aprandia", authorities = "ROLE_USER")
    void IT_test_updateProperty_customUsernameNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"purpose\": \"TO_RENT\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\"\n" +
                "}";

        mockMvc.perform(put("/api/properties/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", is("User was denied with username: aprandia")));
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void IT_test_deleteProperty() throws Exception {
        Property property = entityManager.find(Property.class, Long.valueOf(1));
        assertNotNull(property);
        assertNotEquals(PropertyStatus.INACTIVE, property.getStatus());
        mockMvc.perform(delete("/api/properties/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertEquals(PropertyStatus.INACTIVE, property.getStatus());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void IT_test_propertyNotExists() throws Exception {
        mockMvc.perform(delete("/api/properties/16")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", is("No property found with id: 16")));
    }

    @Test
    @WithMockUser(username = "aprandia", authorities = "ROLE_USER")
    void IT_test_savePropertyImageURLS() throws Exception {

        String inputCommand = "[\n" +
                "{\n" +
                "    \"propertyImageURL\": \"image/jpeg;base64,/1j/4AAQSk...\"\n" +
                "},\n" +
                "{\n" +
                "    \"propertyImageURL\": \"image/jpeg;base64,/2j/4AAQSk...\"\n" +
                "}\n" +
                "]";
        mockMvc.perform(post("/api/properties/imageurls/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].propertyImageURL", is("image/jpeg;base64,/1j/4AAQSk...")))
                .andExpect(jsonPath("$[1].propertyImageURL", is("image/jpeg;base64,/2j/4AAQSk...")));
                }


}
