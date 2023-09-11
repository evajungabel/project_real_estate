package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.CustomUserForm;
import hu.progmasters.moovsmart.dto.CustomUserInfo;
import hu.progmasters.moovsmart.dto.PropertyForm;
import hu.progmasters.moovsmart.dto.PropertyInfo;
import hu.progmasters.moovsmart.service.CustomUserService;
import hu.progmasters.moovsmart.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customusers")
@Slf4j
public class CustomUserController {

    private CustomUserService customUserService;
    private EmailService emailService;

    @Autowired
    public CustomUserController(CustomUserService customUserService, EmailService emailService) {
        this.customUserService = customUserService;
        this.emailService = emailService;
    }

    @GetMapping("/login/me")
    @Operation(summary = "Login customer")
    @ApiResponse(responseCode = "201", description = "Customer is logged in")
//    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<UserDetails> getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Http request, GET /api/customusers, logged in");
        UserDetails loggedInUser = (User) authentication.getPrincipal();
        log.info("GET data from repository/api/customusers, logged in");
        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
    }

    @PostMapping("/registration")
    @Operation(summary = "Save customer")
    @ApiResponse(responseCode = "201", description = "Customer is saved")
    public ResponseEntity<CustomUserInfo> register(@Valid @RequestBody CustomUserForm command) {
        log.info("Http request, POST /api/customusers, body: " + command.toString());
        CustomUserInfo customUserInfo = customUserService.register(command);
        emailService.sendEmail(command.getEmail(), "Felhasználói fiók aktivalása",
                "Kedves " + command.getName() +
                "! \n \n Köszönjük, hogy regisztrált az oldalunkra! \n \n Kérem, kattintson a linkre, hogy visszaigazolja a regisztrációját, amire 30 perce van! \n \n http://localhost:8080/api/customusers/activation/"
                        + customUserService.findCustomUserByEmail(command.getEmail()).getActivation());
        log.info("POST data from repository/api/customusers, body: " + command);
        return new ResponseEntity<>(customUserInfo, HttpStatus.CREATED);
    }

    @GetMapping("/activation/{confirmationToken}")
    @Operation(summary = "Activation confirmation token by costumer")
    @ApiResponse(responseCode = "200", description = "Activation confirmation token by customer")
    public ResponseEntity<String> activation(@Valid @PathVariable("confirmationToken") String confirmationToken) {
        log.info("Http request, GET /api/customusers, activation of confirmation token: " + confirmationToken);
        String result = customUserService.userActivation(confirmationToken);
        log.info("GET /api/customusers, successful activation of confirmation token: " + confirmationToken);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update customer")
    @ApiResponse(responseCode = "200", description = "Customer is updated")
//    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<CustomUserInfo> update(@PathVariable("username") String username,
                                               @Valid @RequestBody CustomUserForm customUserForm) {
        log.info("Http request, PUT /api/customusers/{username} body: " + customUserForm +
                " with variable: " + username);
        CustomUserInfo updated = customUserService.update(username, customUserForm);
        emailService.sendEmail(customUserService.findCustomUserByUsername(username).getEmail(), "Felhasználói fiók adatainak megváltoztatása",
                "Kedves " + customUserService.findCustomUserByUsername(username).getName() +
                        "! \n \n Felhasználói fiókjának adatai megváltoztak! Ha nem Ön tette, mielőbb lépjen kapcsolatba velünk!");
        log.info("PUT data from repository/api/customusers/{customUserId} body: " + customUserForm +
                " with variable: " + username);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping
//    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Get all customers")
    @ApiResponse(responseCode = "200", description = "List of customers")
    public ResponseEntity<List<CustomUserInfo>> getAllCustomers() {
        log.info("Http request, GET /api/list of all customers");
        List<CustomUserInfo> customerInfoList = customUserService.getCustomUsers();
        log.info("GET data from repository/api/list of all customers");
        return new ResponseEntity<>(customerInfoList, HttpStatus.OK);
    }

    @GetMapping("/{username}")
//    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Get customer with username")
    @ApiResponse(responseCode = "200", description = "A customer with username")
    public ResponseEntity<CustomUserInfo> getCustomer(@Valid @PathVariable("username") String username) {
        log.info("Http request, GET /api/customusers customer with username");
        CustomUserInfo customerInfo = customUserService.getCustomUser(username);
        log.info("GET data from repository/api/customusers customer with username");
        return new ResponseEntity<>(customerInfo, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete customer")
    @ApiResponse(responseCode = "200", description = "Customer is deleted")
//    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> deleteUser(@PathVariable("customUsername") String customUsername) {
        log.info("Http request, DELETE /api/customusers/{customUsername} with variable: " + customUsername);
        customUserService.makeInactive(customUsername);
        log.info("DELETE data from repository/api/customusers/{customUsername} with variable: " + customUsername);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/sale/{username}/{propertyId}")
//    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @Operation(summary = "Customer sales a property and it is deleted")
    @ApiResponse(responseCode = "200", description = "Property is sold and deleted by costumer")
    public ResponseEntity<Void> deleteSale(@PathVariable("username") String username, @PathVariable("propertyId") Long pId) {
        log.info("Http request, DELETE /api/customusers/sale/{customUserId}" + username + "{propertyId} with variable: " + pId);
        customUserService.userSale(username, pId);
        log.info("DELETE data from repository/api/customusers/sale/{customUserId}" + username + "{propertyId} with variable: " + pId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{username}/{propertyId}")
//    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @Operation(summary = "Customer deletes a property")
    @ApiResponse(responseCode = "200", description = "Property is deleted by costumer")
    public ResponseEntity<Void> delete(@PathVariable("username") String username, @PathVariable("propertyId") Long pId) {
        log.info("Http request, DELETE /api/customusers/{customUserId}" + username + "{propertyId} with variable: " + pId);
        customUserService.userDelete(username, pId);
        log.info("DELETE data from repository/api/customusers/{customUserId}" + username + "{propertyId} with variable: " + pId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/comment")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @Operation(summary = "Comment estate agent")
    @ApiResponse(responseCode = "201", description = "Comment created")
    public ResponseEntity<Void> comment(@Valid @RequestBody UserComment comment) {
        log.info("Http request, POST /api/customusers/comment, body: " + comment.toString());
        customUserService.comment(comment);
        log.info("POST data from repository/api/customusers/comment, body: " + comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
