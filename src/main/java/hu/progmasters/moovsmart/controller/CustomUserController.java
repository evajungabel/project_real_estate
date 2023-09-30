package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.service.CustomUserService;
import hu.progmasters.moovsmart.service.SendingEmailService;
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
    private SendingEmailService sendingEmailService;

    @Autowired
    public CustomUserController(CustomUserService customUserService, SendingEmailService sendingEmailService) {
        this.customUserService = customUserService;
        this.sendingEmailService = sendingEmailService;
    }


    @PostMapping("/registration")
    @Operation(summary = "Save customer")
    @ApiResponse(responseCode = "201", description = "Customer is saved")
    public ResponseEntity<CustomUserInfo> register(@Valid @RequestBody CustomUserForm customUserForm) {
        log.info("Http request, POST /api/customusers, body: " + customUserForm.toString());
        CustomUserInfo customUserInfo = customUserService.register(customUserForm);
        log.info("POST data from repository/api/customusers, body: " + customUserForm);
        return new ResponseEntity<>(customUserInfo, HttpStatus.CREATED);
    }

    @PostMapping("/login/me")
    @Operation(summary = "Login customer")
    @ApiResponse(responseCode = "201", description = "Customer is logged in")
    public ResponseEntity<CustomUserInfo> login(@RequestBody CustomUserLogInForm customUserLogInForm) {
        log.info("Http request, GET /api/customusers, logged in");
        CustomUserInfo loggedInUser = customUserService.login(customUserLogInForm.getUsername(), customUserLogInForm.getEmail(), customUserLogInForm.getPassword());
        log.info("GET data from repository/api/customusers, logged in");
        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
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

    @GetMapping("/unsubscribenewsletter/{confirmationToken}")
    @Operation(summary = "Unsubscribe from newsletter")
    @ApiResponse(responseCode = "200", description = "Customer unsubscribe from the newsletter")
    public ResponseEntity<String> unsubscribe(@PathVariable("confirmationToken") String confirmationToken) {
        log.info("Http request, GET /api/customusers/unsubscribeNewsletter/{username}, unsubscribe from newsletter by confirmationToken: " + confirmationToken);
        String result = customUserService.userUnsubscribeNewsletter(confirmationToken);
        log.info("GET /api/customusers/unsubscribeNewsletter/{username}, successful unsubscribe from newsletter by confirmationToken: " + confirmationToken);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Get all customers")
    @ApiResponse(responseCode = "200", description = "List of customers")
    public ResponseEntity<List<CustomUserInfo>> getAllCustomers() {
        log.info("Http request, GET /api/list of all customers");
        List<CustomUserInfo> customerInfoList = customUserService.getCustomUsers();
        log.info("GET data from repository/api/list of all customers");
        return new ResponseEntity<>(customerInfoList, HttpStatus.OK);
    }

    @GetMapping("/customuser")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    @Operation(summary = "Get a customer with username by customer")
    @ApiResponse(responseCode = "200", description = "Get a customer with username by a customer")
    public ResponseEntity<CustomUserInfo> getCustomUserDetails() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, GET /api/customusers get a customer by customer with username: " + userDetails.getUsername());
        CustomUserInfo customerInfoList = customUserService.getCustomUserDetails(userDetails.getUsername());
        log.info("GET data from repository/api/customusers get a customer by customer with username: " + userDetails.getUsername());
        return new ResponseEntity<>(customerInfoList, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Get a customer with username by admin")
    @ApiResponse(responseCode = "200", description = "Get a customer with username by admin.")
    public ResponseEntity<CustomUserInfo> getCustomUserDetails(@PathVariable("username") String username) {
        log.info("Http request, GET /api/customusers get a customer by admin with username: " + username);
        CustomUserInfo customerInfoList = customUserService.getCustomUserDetails(username);
        log.info("GET data from repository/api/customusers get a customer by admin with username: " + username);
        return new ResponseEntity<>(customerInfoList, HttpStatus.OK);
    }


    @PutMapping()
    @Operation(summary = "Update customer")
    @ApiResponse(responseCode = "200", description = "Customer is updated")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<CustomUserInfo> update(@Valid @RequestBody CustomUserForm customUserForm) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, PUT /api/customusers/{username} body: " + customUserForm +
                " with variable: " + userDetails.getUsername());
        CustomUserInfo updated = customUserService.update(userDetails.getUsername(), customUserForm);
        log.info("PUT data from repository/api/customusers/{customUserId} body: " + customUserForm +
                " with variable: " + userDetails.getUsername());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }


    @PutMapping("/{username}")
    @Operation(summary = "Update customer")
    @ApiResponse(responseCode = "200", description = "Customer is updated")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<CustomUserInfo> update(@PathVariable("username") String username,
                                                 @Valid @RequestBody CustomUserForm customUserForm) {
        log.info("Http request, PUT /api/customusers/{username} body: " + customUserForm +
                " with variable: " + username);
        CustomUserInfo updated = customUserService.update(username, customUserForm);
        log.info("PUT data from repository/api/customusers/{customUserId} body: " + customUserForm +
                " with variable: " + username);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }


    @DeleteMapping()
    @Operation(summary = "Delete customer")
    @ApiResponse(responseCode = "200", description = "Customer is deleted")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<String> deleteUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, DELETE /api/customusers with variable: " + userDetails.getUsername());
        String message = customUserService.makeInactive(userDetails.getUsername());
        log.info("DELETE data from repository/api/customusers with variable: " + userDetails.getUsername());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/{customUsername}")
    @Operation(summary = "Delete customer")
    @ApiResponse(responseCode = "200", description = "Customer is deleted")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> deleteUser(@PathVariable("customUsername") String customUsername) {
        log.info("Http request, DELETE /api/customusers/{customUsername} with variable: " + customUsername);
        String message = customUserService.makeInactive(customUsername);
        log.info("DELETE data from repository/api/customusers/{customUsername} with variable: " + customUsername);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }



    @DeleteMapping("/sale/{propertyId}")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    @Operation(summary = "Customer sales a property and it is deleted")
    @ApiResponse(responseCode = "200", description = "Property is sold and deleted by costumer")
    public ResponseEntity<String> deleteSale(@PathVariable("propertyId") Long pId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, DELETE /api/customusers/sale" + userDetails.getUsername() + "{propertyId} with variable: " + pId);
        String message = customUserService.deleteSale(userDetails.getUsername(), pId);
        log.info("DELETE data from repository/api/customusers/sale " + userDetails.getUsername() + "{propertyId} with variable: " + pId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/sale/{username}/{propertyId}")
    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Customer sales a property and it is deleted")
    @ApiResponse(responseCode = "200", description = "Property is sold and deleted by costumer")
    public ResponseEntity<String> deleteSale(@PathVariable("username") String username, @PathVariable("propertyId") Long pId) {
        log.info("Http request, DELETE /api/customusers/sale" + username + "{propertyId} with variable: " + pId);
        String message = customUserService.deleteSale(username, pId);
        log.info("DELETE data from repository/api/customusers/sale " + username + "{propertyId} with variable: " + pId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{propertyId}")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    @Operation(summary = "Customer deletes a property")
    @ApiResponse(responseCode = "200", description = "Property is deleted by costumer")
    public ResponseEntity<String> delete(@PathVariable("propertyId") Long pId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, DELETE /api/customusers by" + userDetails.getUsername() + "{propertyId} with variable: " + pId);
        String message = customUserService.deleteProperty(userDetails.getUsername(), pId);
        log.info("DELETE data from repository/api/customusers by" + userDetails.getUsername() + "{propertyId} with variable: " + pId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/comment")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    @Operation(summary = "Comment estate agent")
    @ApiResponse(responseCode = "201", description = "Comment created")
    public ResponseEntity<Void> comment(@Valid @RequestBody CommentForm comment) {
        log.info("Http request, POST /api/customusers/comment, body: " + comment.toString());
        customUserService.comment(comment);
        log.info("POST data from repository/api/customusers/comment, body: " + comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/comment/{userName}")
    @Operation(summary = "List comments from agent")
    @ApiResponse(responseCode = "200", description = "Comment listed")
    public ResponseEntity<EstateAgentInfo> listComments(@PathVariable("userName") String userName) {
        log.info("Http request, GET /api/customusers/comment/ " + userName);
        EstateAgentInfo estateAgentInfo = customUserService.getAgentInfo(userName);
        log.info("GET data from repository/api/customusers/comment/{userName} " + userName);
        return new ResponseEntity<>(estateAgentInfo, HttpStatus.OK);
    }

    @PostMapping("/register-admin")
    @Operation(summary = "Register first admin")
    @ApiResponse(responseCode = "201", description = "First admin is saved")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody CustomUserFormAdmin customUserFormAdmin) {
        log.info("Http request, POST /api/customusers, body: " + customUserFormAdmin.toString());
        if (customUserService.countByIsAdminTrue() == 0 && customUserFormAdmin.getQuestion().equals("tetőcserép")) {
            customUserService.registerAdmin(customUserFormAdmin);
            log.info("POST data from repository/api/customusers, body: " + customUserFormAdmin);
            return new ResponseEntity<>("Admin registration was successful!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("The admin registration is closed!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/roleadmin/{username}")
    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Giving ROLE_ADMIN")
    @ApiResponse(responseCode = "200", description = "The ROLE_ADMIN was given.")
    public ResponseEntity<CustomUserInfo> giveRoleAdmin(@PathVariable("username") String username) {
        log.info("Http request, PUT /api/customusers, body : giving ROLE_ADMIN");
        CustomUserInfo customUserInfo = customUserService.giveRoleAdmin(username);
        log.info("PUT data from repository/api/customusers : the ROLE_ADMIN was given");
        return new ResponseEntity<>(customUserInfo, HttpStatus.OK);
    }


    @DeleteMapping("/total/{customUsername}")
    @Operation(summary = "Delete customer")
    @ApiResponse(responseCode = "200", description = "Customer is deleted")
    public ResponseEntity<String> deleteUserTotal(@PathVariable("customUsername") String customUsername) {
        log.info("Http request, DELETE /api/customusers/{customUsername} with variable: " + customUsername);
        String message = customUserService.deleteUser(customUsername);
        log.info("DELETE data from repository/api/customusers/{customUsername} with variable: " + customUsername);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}


