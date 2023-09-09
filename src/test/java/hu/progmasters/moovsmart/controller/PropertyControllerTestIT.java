package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.domain.PropertyStatus;
import hu.progmasters.moovsmart.domain.PropertyType;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        mockMvc.perform(get("/api/properties"))
                .andExpect(status().isOk());
    }

    @Test
    void IT_test_findAllProperties() throws Exception {
        mockMvc.perform(get("/api/properties")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Eladó Balatoni Ház")))
                .andExpect(jsonPath("$[1].name", is("Eladó ház")))
                .andExpect(jsonPath("$[2].name", is("Eladó lakás Pécsett")))
                .andExpect(jsonPath("$[3].name", is("Eladó lakás a városban")))
                .andExpect(jsonPath("$[4].name", is("Eladó ház")))
                .andExpect(jsonPath("$[5].name", is("Kiadó lakás")))
                .andExpect(jsonPath("$[6].name", is("Eladó irodaház")))
                .andExpect(jsonPath("$[7].name", is("Eladó lakás")))
                .andExpect(jsonPath("$[8].name", is("Eladó villa")))
                .andExpect(jsonPath("$[9].name", is("Eladó lakás")))
                .andExpect(jsonPath("$[10].name", is("Eladó ház")))
                .andExpect(jsonPath("$[11].name", is("Eladó villa")))
                .andExpect(jsonPath("$[12].name", is("Eladó lakás")))
                .andExpect(jsonPath("$[13].name", is("Eladó ház")))
                .andExpect(jsonPath("$[14].name", is("Eladó ház")));
    }

    @Test
    void IT_test_getPropertyDetails() throws Exception {
        mockMvc.perform(get("/api/properties/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Eladó Balatoni Ház")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Eladó családi ház a Balton partján")))
                .andExpect(jsonPath("$.imageUrl", is("www.kep-url/122324gfg/kep.hu")))
                .andExpect(jsonPath("$.numberOfRooms", is(4)))
                .andExpect(jsonPath("$.price", is(63000000)));
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
    void IT_test_saveProperty() throws Exception {

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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Eladó Ház")))
                .andExpect(jsonPath("$.numberOfRooms", is(4)))
                .andExpect(jsonPath("$.price", is(63000000)))
                .andExpect(jsonPath("$.imageUrl", is("image/jpeg;base68,/9j783/4Adfhdk")));
    }

    @Test
    void IT_test_saveProperty_nameNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"    \",\n" +
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
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Property name cannot be empty!")));
    }

    @Test
    void IT_test_saveProperty_nameSizeNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\",\n" +
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
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Property name must be between 1 and 200 characters!")));
    }

    @Test
    void IT_test_saveProperty_typeNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
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
                .andExpect(jsonPath("$.fieldErrors[0].field", is("type")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Property type cannot be empty!")));
    }


    @Test
    void IT_test_saveProperty_areaMinNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
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
    void IT_test_saveProperty_areaMaxNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
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
    void IT_test_saveProperty_numberOfRoomsMinNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
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
    void IT_test_saveProperty_numberOfRoomsMaxNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
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
    void IT_test_saveProperty_priceNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
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
    void IT_test_saveProperty_customUsernameNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
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
                .andExpect(jsonPath("$.fieldErrors[0].field", is("customUsername")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Username cannot be empty!")));
    }


    @Test
    void IT_test_updateProperty() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"FLAT\",\n" +
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
                .andExpect(jsonPath("$.price", is(75000000)))
                .andExpect(jsonPath("$.imageUrl", is("image/jpeg;base68,/9j783/4Adfhdk")));
    }

    @Test
    void IT_test_updateProperty_nameNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"    \",\n" +
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
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Property name cannot be empty!")));
    }

    @Test
    void IT_test_updateProperty_nameSizeNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\",\n" +
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
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Property name must be between 1 and 200 characters!")));
    }

    @Test
    void IT_test_updateProperty_typeNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
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
    void IT_test_updateProperty_areaMinNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
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
    void IT_test_updateProperty_areaMaxNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
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
    void IT_test_updateProperty_numberOfRoomsMinNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
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
    void IT_test_updateProperty_numberOfRoomsMaxNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
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
    void IT_test_updateProperty_priceNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
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
    void IT_test_updateProperty_customUsernameNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Eladó Ház\",\n" +
                "    \"type\": \"HOUSE\",\n" +
                "    \"area\": 120,\n" +
                "    \"numberOfRooms\": 6,\n" +
                "    \"price\": 75000000,\n" +
                "    \"description\": \"Kényelmes családi ház\",\n" +
                "    \"imageUrl\": \"image/jpeg;base68,/9j783/4Adfhdk\"\n" +
                "}";

        mockMvc.perform(put("/api/properties/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("customUsername")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Username cannot be empty!")));
    }


    @Test
    void IT_test_deleteProperty() throws Exception {
        Property property = entityManager.find(Property.class, Long.valueOf(1));
        assertTrue(property != null);
        assertNotEquals(property.getStatus(), PropertyStatus.INACTIVE);
        mockMvc.perform(delete("/api/properties/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertEquals(property.getStatus(), PropertyStatus.INACTIVE);
    }

    @Test
    void IT_test_propertyNotExists() throws Exception {
        mockMvc.perform(delete("/api/properties/16")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", is("No property found with id: 16")));
    }

}
