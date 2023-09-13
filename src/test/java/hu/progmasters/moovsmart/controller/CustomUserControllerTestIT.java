package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.service.CustomUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;


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

    //TODO logintest

//    @Test
//    void IT_test_loginCustomUser() throws Exception {
//        UserDetails userDetails = User
//                .withUsername("aprandia")
//                .password("password")
//                .authorities(AuthorityUtils.createAuthorityList(String.valueOf(CustomUserRole.ROLE_USER)))
//                .build();
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authentication);
//
//        mockMvc.perform(get("/api/customusers/login/me"))
//                .andExpect(status().isOk());
//
//
//        mockMvc.perform(get("/api/customusers/login/me"))
//                .andExpect(status().isOk());
//    }

//        }

//        mockMvc.perform(get("/api/customusers/login/me").with(userDetails(userDetails)))
//            .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk());


    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().isNoContent());
    }


    @Test
    void IT_test_ActivationTokenIsActive() throws Exception {
        mockMvc.perform(get("/api/customusers/activation/111111")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void IT_test_ActivationTokenIsExpired() throws Exception {
        mockMvc.perform(get("/api/customusers/activation/654321")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void IT_test_registerCustomUser() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(jsonPath("$.name", is("Bogyó és Babóca")))
                .andExpect(jsonPath("$.username", is("bogyóésbabóca")))
                .andExpect(jsonPath("$.email", is("bogyo.es.baboca@gmail.com")))
                .andExpect(jsonPath("$.phoneNumber", is("+36306363634")))
                .andExpect(jsonPath("$.customUserRoles", is(List.of("ROLE_USER"))))
                .andExpect(status().isCreated());
    }

    //TODO for email sending

    @Test
    void IT_test_saveCustomUser_nameIsBlank() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"           \",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
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
    void IT_test_saveCustomUser_nameMinSize() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"B\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Name must be between 3 and 200 characters!")));
    }

    @Test
    void IT_test_saveCustomUser_nameMaxSize() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca Bogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Name must be between 3 and 200 characters!")));
    }

    @Test
    void IT_test_saveCustomUser_usernameNotBlank() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"          \",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"isAgent\": \"false\",\n" +
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
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"isAgent\": \"false\",\n" +
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
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"isAgent\": \"false\",\n" +
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bi@c.ko\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("E-mail must be between 8 and 200 characters!")));
    }


    @Test
    void IT_test_saveCustomUser_emailSizeMaxNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbab.oca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("E-mail must be between 8 and 200 characters!")));
    }

    @Test
    void IT_test_saveCustomUser_emailPatternNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.babocagmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)));
    }


    @Test
    void IT_test_saveCustomUser_phoneNumberPatternNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306.634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(post("/api/customusers/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("phoneNumber")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("must match \"^(\\+{0,})(\\d{0,})([(]{1}\\d{1,3}[)]{0,}){0,}(\\s?\\d+|\\+\\d{2,3}\\s{1}\\d+|\\d+){1}[\\s|-]?\\d+([\\s|-]?\\d+){1,2}(\\s){0,}$\"")));
    }

    @Test
    void IT_test_updateCustomUser() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyoesbaboca\",\n" +
                "    \"password\": \"bESb1234*\",\n" +
                "    \"isAgent\": \"true\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";


        mockMvc.perform(put("/api/customusers/aprandia")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(jsonPath("$.name", is("Bogyó és Babóca")))
                .andExpect(jsonPath("$.username", is("bogyoesbaboca")))
                .andExpect(jsonPath("$.email", is("bogyo.es.baboca@gmail.com")))
                .andExpect(jsonPath("$.phoneNumber", is("+36306363634")))
                .andExpect(jsonPath("$.customUserRoles", is(List.of("ROLE_USER"))))
                .andExpect(status().isOk());
    }



    @Test
    void IT_test_updateCustomUser_nameIsBlank() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"           \",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Name cannot be empty!")));
    }

    @Test
    void IT_test_updateCustomUser_nameMinSize() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"B\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Name must be between 3 and 200 characters!")));
    }

    @Test
    void IT_test_updateCustomUser_nameMaxSize() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca Bogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és BabócaBogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("name")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Name must be between 3 and 200 characters!")));
    }

    @Test
    void IT_test_updateCustomUser_usernameNotBlank() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"          \",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"password\": \"BesB1234*\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
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
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bi@c.ko\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("E-mail must be between 8 and 200 characters!")));
    }


    @Test
    void IT_test_updateCustomUser_emailSizeMaxNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbabocabogyoesbab.oca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)));
    }

    @Test
    void IT_test_updateCustomUser_emailPatternNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306363634\",\n" +
                "    \"email\": \"bogyo.es.babocagmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("email")))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)));
    }

    @Test
    void IT_test_updateCustomUser_phoneNumberPatternNotValid() throws Exception {

        String inputCommand = "{\n" +
                "    \"name\": \"Bogyó és Babóca\",\n" +
                "    \"username\": \"bogyóésbabóca\",\n" +
                "    \"password\": \"BesB1234*\",\n" +
                "    \"isAgent\": \"false\",\n" +
                "    \"hasNewsletter\": \"true\",\n" +
                "    \"phoneNumber\": \"+36306.634\",\n" +
                "    \"email\": \"bogyo.es.baboca@gmail.com\"\n" +
                "}";

        mockMvc.perform(put("/api/customusers/aprandia")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field", is("phoneNumber")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("must match \"^(\\+{0,})(\\d{0,})([(]{1}\\d{1,3}[)]{0,}){0,}(\\s?\\d+|\\+\\d{2,3}\\s{1}\\d+|\\d+){1}[\\s|-]?\\d+([\\s|-]?\\d+){1,2}(\\s){0,}$\"")));
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
        mockMvc.perform(get("/api/customusers/aprandia")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("aprandia")))
                .andExpect(jsonPath("$.name", is("Avivah Prandi")))
                .andExpect(jsonPath("$.email", is("aprandia@miitbeian.gov.cn")))
                .andExpect(jsonPath("$.phoneNumber", is("+36306363631")));
    }


    @Test
    void IT_test_getCustomUsersWithNoId() throws Exception {
        mockMvc.perform(get("/api/customusers/bobobo")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("User not found error.")))
                .andExpect(jsonPath("$.details", is("Username was not found with: bobobo")));
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
                .andExpect(jsonPath("$.details", is("Username was not found with: 21")));
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
        assertNotNull(property.getDateOfSale());
    }

    @Test
    void IT_test_customUserSalePropertyCustomUserNotExists() throws Exception {
        mockMvc.perform(delete("/api/customusers/sale/anemletezo/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("User not found error.")))
                .andExpect(jsonPath("$.details", is("Username was not found with: anemletezo")));
    }


    @Test
    void IT_test_customUserDeleteProperty() throws Exception {
        CustomUser customUser = entityManager.find(CustomUser.class, Long.valueOf(3));
        assertNotNull(customUser);

        Property property = entityManager.find(Property.class, Long.valueOf(6));
        assertNotNull(property);
        assertNotNull(property.getDateOfActivation());

        assertEquals(customUser.getPropertyList().get(0), property);

        mockMvc.perform(delete("/api/customusers/ikoubek4/6")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertNotNull(property.getDateOfInactivation());
    }

    @Test
    void IT_test_customUserDeletePropertyIfCustomUserNotExists() throws Exception {
        mockMvc.perform(delete("/api/customusers/holvan/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("User not found error.")))
                .andExpect(jsonPath("$.details", is("Username was not found with: holvan")));
    }

    //TODO for comment

}

