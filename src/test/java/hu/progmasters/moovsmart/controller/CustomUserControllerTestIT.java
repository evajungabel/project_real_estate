package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.service.CustomUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


    @Autowired
    CustomUserService customUserService;

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
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";


        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isCreated());
    }

    //TODO for email sending

    @Test
    void IT_test_saveCustomUser_nameNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"           \",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Name cannot be empty!")));
    }

    @Test
    void IT_test_saveCustomUser_usernameNotBlank() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"          \",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("username")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Username cannot be empty!")));
    }


    @Test
    void IT_test_saveCustomUser_usernameSizeMinNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"b\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("username")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Name must be between 3 and 200 characters!")));
    }


    @Test
    void IT_test_saveCustomUser_usernameSizeMaxNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("username")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Name must be between 3 and 200 characters!")));
    }

    @Test
    void IT_test_saveCustomUser_passwordNotNull() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("password")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Password cannot be empty!")));
    }


    @Test
    void IT_test_saveCustomUser_passwordSizeMinNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB12*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("password")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("must match \"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$\"")));
    }


    @Test
    void IT_test_saveCustomUser_passwordSizeMaxNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234567890123456789*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("password")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("must match \"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$\"")));
    }

    @Test
    void IT_test_saveCustomUser_passwordPatternNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234567890123456789\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("password")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("must match \"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$\"")));
    }

    @Test
    void IT_test_saveCustomUser_emailNotNull() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("E-mail cannot be empty!")));
    }


    @Test
    void IT_test_saveCustomUser_emailSizeMinNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"b@c\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("must match \"^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$\"")));
    }


    @Test
    void IT_test_saveCustomUser_emailSizeMaxNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)));
//                .andExpect(jsonPath("$.fieldErrors[1].field", is("email")))
//                .andExpect(jsonPath("$.fieldErrors[1].message", is("E-mail must be between 3 and 200 characters!")));
    }

    @Test
    void IT_test_saveCustomUser_emailPatternNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.es.babocagmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)));
//                .andExpect(jsonPath("$.fieldErrors[1].field", is("email")))
//                .andExpect(jsonPath("$.fieldErrors[1].message", is("must be a well-formed email address")));
    }


    //TODO logintest

    //TODO logout test

    @Test
    void IT_test_loginCustomUser() throws Exception {
        UserDetails userDetails = User
                .withUsername("username")
                .password("password")
                .authorities(AuthorityUtils.createAuthorityList(String.valueOf(CustomUserRole.ROLE_USER))) // Set user roles/authorities
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        mockMvc.perform(get("/api/customusers/login/me").with((RequestPostProcessor) userDetails))
                .andExpect(status().isOk());


//        mockMvc.perform(get("/api/customusers/login/me"))
//                .andExpect(status().isOk());
    }

//        }

//        mockMvc.perform(get("/api/customusers/login/me").with(userDetails(userDetails)))
//            .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk());


    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().isNoContent());
    }

//TODO activation confirmation token

    @Test
    void IT_test_ActivationToken() throws Exception {
        mockMvc.perform(get("/api/customusers/activation/123456")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }


    @Test
    void IT_test_updateCustomUser() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyoesbaboca\",\n" +
                "    \"password\": \"bESb1234*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";


        mockMvc.perform(put("/api/customusers/aprandia")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isOk());
    }


    //TODO for email sending

    @Test
    void IT_test_updateCustomUser_nameNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"           \",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Name cannot be empty!")));
    }

    @Test
    void IT_test_updateCustomUser_usernameNotBlank() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"          \",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("username")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Username cannot be empty!")));
    }


    @Test
    void IT_test_updateCustomUser_usernameSizeMinNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"b\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("username")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Name must be between 3 and 200 characters!")));
    }


    @Test
    void IT_test_updateCustomUser_usernameSizeMaxNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("username")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Name must be between 3 and 200 characters!")));
    }

    @Test
    void IT_test_updateCustomUser_passwordNotNull() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("password")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Password cannot be empty!")));
    }


    @Test
    void IT_test_updateCustomUser_passwordSizeMinNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB12*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("password")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("must match \"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$\"")));
    }


    @Test
    void IT_test_updateCustomUser_passwordSizeMaxNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234567890123456789*\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("password")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("must match \"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$\"")));
    }

    @Test
    void IT_test_updateCustomUser_passwordPatternNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234567890123456789\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("password")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("must match \"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$\"")));
    }

    @Test
    void IT_test_updateCustomUser_emailNotNull() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("E-mail cannot be empty!")));
    }


    @Test
    void IT_test_updateCustomUser_emailSizeMinNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"b@c\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("must match \"^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$\"")));
    }


    @Test
    void IT_test_updateCustomUser_emailSizeMaxNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.babocabogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("E-mail must be between 3 and 200 characters!")))
                .andExpect(jsonPath("$.fieldErrors[1].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors[1].message", is("must be a well-formed email address")));
    }

    @Test
    void IT_test_updateCustomUser_emailPatternNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"email\": \"bogyo.es.babocagmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("must match \"^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$\"")))
                .andExpect(jsonPath("$.fieldErrors[1].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors[1].message", is("must be a well-formed email address")));
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

    @Test
    void IT_test_getCustomUser() throws Exception {
        mockMvc.perform(get("/api/customusers/aprandia")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("aprandia")));
    }


    @Test
    void IT_test_getCustomUsersWithNoId() throws Exception {
        mockMvc.perform(get("/api/customusers/bobobo")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("User not found error.")))
                .andExpect(jsonPath("$.details", is("bobobo")));
    }


    @Test
    void IT_test_deleteCustomUser() throws Exception {
        CustomUser customUser = entityManager.find(CustomUser.class, Long.valueOf(2));
        assertTrue(customUser != null);
        assertFalse(customUser.isDeleted());
        List<Property> propertyList = customUser.getPropertyList();
        mockMvc.perform(delete("/api/customusers/glockley5")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertTrue(customUser.isDeleted());
        assertEquals(propertyList.size(), 1);
        assertEquals(propertyList.get(0).getName(), "Eladó ház");


    }

    @Test
    void IT_test_customUserNotExists() throws Exception {
        mockMvc.perform(delete("/api/customusers/21")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("User not found error.")))
                .andExpect(jsonPath("$.details", is("21")));
    }

    @Test
    void IT_test_customUserSaleProperty() throws Exception {
        CustomUser customUser = entityManager.find(CustomUser.class, Long.valueOf(2));
        assertTrue(customUser != null);

        Property property = entityManager.find(Property.class, Long.valueOf(2));
        assertTrue(property != null);
        assertFalse(property.getDateOfSale() != null);

        assertEquals(customUser.getPropertyList().get(0), property);

        mockMvc.perform(delete("/api/customusers/sale/glockley5/2")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertTrue(property.getDateOfSale() != null);
    }

    @Test
    void IT_test_customUserSalePropertyCustomUserNotExists() throws Exception {
        mockMvc.perform(delete("/api/customusers/sale/anemletezo/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("User not found error.")))
                .andExpect(jsonPath("$.details", is("anemletezo")));
    }


    @Test
    void IT_test_customUserDeleteProperty() throws Exception {
        CustomUser customUser = entityManager.find(CustomUser.class, Long.valueOf(3));
        assertTrue(customUser != null);

        Property property = entityManager.find(Property.class, Long.valueOf(6));
        assertTrue(property != null);
        assertTrue(property.getDateOfActivation() != null);

        assertEquals(customUser.getPropertyList().get(0), property);

        mockMvc.perform(delete("/api/customusers/ikoubek4/6")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertTrue(property.getDateOfInactivation() != null);
    }

    @Test
    void IT_test_customUserDeletePropertyIfCustomUserNotExists() throws Exception {
        mockMvc.perform(delete("/api/customusers/holvan/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("User not found error.")))
                .andExpect(jsonPath("$.details", is("holvan")));
    }

}
