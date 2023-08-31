package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.CustomUserForm;
import hu.progmasters.moovsmart.dto.CustomUserInfo;
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
//@Secured({"ROLE_GUEST"})
public class CustomUserController {

    private CustomUserService customUserService;

    private EmailService emailService;

    @Autowired
    public CustomUserController(CustomUserService customUserService, EmailService emailService) {
        this.customUserService = customUserService;
        this.emailService = emailService;
    }


    @PostMapping("/registration")
    @Operation(summary = "Save customer")
    @ApiResponse(responseCode = "201", description = "Customer is saved")
    public ResponseEntity<Void> register(@Valid @RequestBody CustomUserForm command) {
        log.info("Http request, POST /api/customusers, body: " + command.toString());
        customUserService.register(command);
        log.info("POST data from repository/api/customusers, body: " + command);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



}
