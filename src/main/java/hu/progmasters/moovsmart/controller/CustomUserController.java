package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.CustomUserForm;
import hu.progmasters.moovsmart.service.CustomUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class CustomUserController {

    private CustomUserService customUserService;


    @Autowired
    public CustomUserController(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }


    @PostMapping
    @Operation(summary = "Save customer")
    @ApiResponse(responseCode = "201", description = "Customer saved")
    public ResponseEntity<Void> save(@Valid @RequestBody CustomUserForm command) {
        log.info("Http request, POST /api/user, body: " + command.toString());
        customUserService.save(command);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{customUserId}/{propertyId}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @Operation(summary = "Delete customer")
    @ApiResponse(responseCode = "200", description = "Account deleted")
    public ResponseEntity<Void> delete(@PathVariable("customUserId") Long cId, @PathVariable("propertyId") Long pId) {
        log.info("Http request, DELETE /api/property/{customUserId}" + cId + "{propertyId} with variable: " + pId);
        customUserService.userDelete(cId, pId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/sale/{customUserId}/{propertyId}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @Operation(summary = "Delete a customer property")
    @ApiResponse(responseCode = "200", description = "Property deleted")
    public ResponseEntity<Void> deleteSale(@PathVariable("customUserId") Long cId, @PathVariable("propertyId") Long pId) {
        log.info("Http request, DELETE /api/property/{customUserId}" + cId + "{propertyId} with variable: " + pId);
        customUserService.userSale(cId, pId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
