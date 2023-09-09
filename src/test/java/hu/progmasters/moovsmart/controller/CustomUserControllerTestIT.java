package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.domain.CustomUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class CustomUserControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;


    @Test
    void IT_test_atStart_emptyList() throws Exception {
        mockMvc.perform(get("/api/customusers"))
                .andExpect(status().isOk());
    }

    @Test
    void IT_test_registerCustomUser() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.esbaboca@gmail.com\"\n" +
                "}";


        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isCreated());
    }

    @Test
    void IT_test_nameNotValid() throws Exception {

    }

    @Test
    void IT_test_usernameNotValid() throws Exception {

    }

    @Test
    void IT_test_passwordNotValid() throws Exception {
    }


    @Test
    void IT_test_emailNotValid() throws Exception {
    }



    @Test
    void IT_test_findAllCustomUsers() throws Exception {
        mockMvc.perform(get("/api/customusers")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is("aprandia")))
                .andExpect(jsonPath("$[1].username", is("glockley5")))
                .andExpect(jsonPath("$[2].username", is("ikoubek4")))
                .andExpect(jsonPath("$[3].username", is("cduprec")))
                .andExpect(jsonPath("$[4].username", is("dknottonb")))
                .andExpect(jsonPath("$[5].username", is("cglowacha3")))
                .andExpect(jsonPath("$[6].username", is("sbenzingi")))
                .andExpect(jsonPath("$[7].username", is("czambonini8")))
                .andExpect(jsonPath("$[8].username", is("ikennadyg")))
                .andExpect(jsonPath("$[9].username", is("kshard7")))
                .andExpect(jsonPath("$[10].username", is("bmoyes2")))
                .andExpect(jsonPath("$[11].username", is("erobej")))
                .andExpect(jsonPath("$[12].username", is("wmcinteer9")))
                .andExpect(jsonPath("$[13].username", is("fmartijn0")))
                .andExpect(jsonPath("$[14].username", is("dmugef")))
                .andExpect(jsonPath("$[15].username", is("gfrossell1")))
                .andExpect(jsonPath("$[16].username", is("wchaterd")))
                .andExpect(jsonPath("$[17].username", is("dfilyukovh")))
                .andExpect(jsonPath("$[18].username", is("gjakubczyke")))
                .andExpect(jsonPath("$[19].username", is("cgasking6")));
    }

    //TODO in the constroller
    @Test
    void IT_test_getCustomUser() throws Exception {
        mockMvc.perform(get("/api/customusers/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("aprandia")));
    }


    @Test
    void IT_test_getCustomUsersWithNoId() throws Exception {
        mockMvc.perform(get("/api/customusers/21")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("id")))
                .andExpect(jsonPath("$[0].errorMessage", is("No customuser found with id: 21")));
    }




    @Test
    void IT_test_updateCustomUser() throws Exception {

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


        mockMvc.perform(put("/api/customuser/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isOk());
    }

    //TODO validations


    @Test
    void IT_test_deleteCustomUser() throws Exception {
        CustomUser customUser = entityManager.find(CustomUser.class, Long.valueOf(1));
        assertTrue(customUser != null);
        assertFalse(customUser.isDeleted());
        mockMvc.perform(delete("/api/customusers/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertTrue(customUser.isDeleted());

//TODO check property status

//        mockMvc.perform(delete("/api/properties/1")
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status", is("INACTIVE")));
    }

    @Test
    void IT_test_customUserNotExists() throws Exception {
        mockMvc.perform(delete("/api/customusers/21")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("customUserId")))
                .andExpect(jsonPath("$[0].errorMessage", is("No customuser found with id: 21")));
    }

    @Test
    void IT_test_customUserSaleProperty() throws Exception {

    }

    //TODO exceptions


    @Test
    void IT_test_customUserDeleteProperty() throws Exception {

    }

    //TODO exceptions


}
