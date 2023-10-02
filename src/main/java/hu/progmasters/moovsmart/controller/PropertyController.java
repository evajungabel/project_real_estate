package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.AuthenticationExceptionImpl;
import hu.progmasters.moovsmart.service.PropertyService;
import hu.progmasters.moovsmart.validation.PropertyDataFormYearValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@Slf4j
public class PropertyController {

    private PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService, PropertyDataFormYearValidator propertyDataFormYearValidator) {
        this.propertyService = propertyService;
    }


    @GetMapping("/allproperties")
    @Operation(summary = "Getting all properties by anybody")
    @ApiResponse(responseCode = "200", description = "List of properties by anybody.")
    public ResponseEntity<List<PropertyDetails>> getAllProperties() {
        log.info("Http request, GET /api/properties/allproperties");
        List<PropertyDetails> propertyList = propertyService.getProperties();
        log.info("GET data of repository from /api/properties/allproperties");
        return new ResponseEntity<>(propertyList, HttpStatus.OK);
    }


    @GetMapping(params = {"page", "size", "sortDir", "sort"})
    @Operation(summary = "Getting list of paginated and sorted property by anybody")
    @ApiResponse(responseCode = "200", description = "Paginated and sorted list of property is got by anybody.")
    public ResponseEntity<List<PropertyDetails>> findPaginatedAndSorted(
            @RequestParam("sortDir") String sortDir,
            @RequestParam("sort") String sort,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        log.info("Http request, GET /api/properties, with variables: " + page + size + sort + sortDir);
        List<PropertyDetails> propertyDetailsList = propertyService.getPropertyListPaginatedAndSorted(page, size, sortDir, sort);
        log.info("GET data of repository from /api/properties, with variable: " + page + size + sort + sortDir);
        return new ResponseEntity<>(propertyDetailsList, HttpStatus.OK);
    }


    @PostMapping("/requests")
    @Operation(summary = "Getting filtered, paginated and sorted list of property by anybody")
    @ApiResponse(responseCode = "200", description = "Paginated, sorted and filtered list of property is got by anybody.")
    public ResponseEntity<List<PropertyFilterRequestInfo>> findByRequestedValues(
            @RequestParam("sortDir") String sortDir,
            @RequestParam("sort") String sort,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @Valid @RequestBody PropertyFilterRequestForm propertyFilterRequestForm) {
        log.info("Http request, POST /api/properties/requests, with variables: " + page + size + sort + sortDir + propertyFilterRequestForm);
        List<PropertyFilterRequestInfo> propertyFilterRequestInfos = propertyService.getPropertyRequests(page, size, sortDir, sort, propertyFilterRequestForm);
        log.info("POST data of repository from /api/properties/requests, with variable: " + page + size + sort + sortDir + propertyFilterRequestForm);
        return new ResponseEntity<>(propertyFilterRequestInfos, HttpStatus.OK);
    }

    @GetMapping("/{propertyId}")
    @Operation(summary = "Getting property with {propertyId} by anybody")
    @ApiResponse(responseCode = "200", description = "Getting property details by anybody.")
    public ResponseEntity<PropertyDetails> getPropertyDetails(@PathVariable("propertyId") Long id) {
        log.info("Http request, GET /api/properties/{propertyId}, with variable: " + id);
        PropertyDetails propertyDetails = propertyService.getPropertyDetails(id);
        log.info("GET data of repository from /api/properties/{propertyId}, with variable: " + id);
        return new ResponseEntity<>(propertyDetails, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Saving property by customer")
    @ApiResponse(responseCode = "201", description = "Property saved by customer.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<PropertyInfo> createProperty(@RequestBody @Valid PropertyForm propertyForm) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, POST /api/properties, body: " + propertyForm.toString());
        PropertyInfo propertyInfo = propertyService.createProperty(userDetails.getUsername(), propertyForm);
        log.info("POST data of repository from /api/properties, body: " + propertyForm);
        return new ResponseEntity<>(propertyInfo, HttpStatus.CREATED);
    }

    @PostMapping("/{username}")
    @Operation(summary = "Saving property by admin")
    @ApiResponse(responseCode = "201", description = "Property saved by admin.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<PropertyInfo> createProperty(@PathVariable("username") String username, @RequestBody @Valid PropertyForm propertyForm) {
        log.info("Http request, POST /api/properties/{username}, body: " + propertyForm.toString());
        PropertyInfo propertyInfo = propertyService.createProperty(username, propertyForm);
        log.info("POST data of repository from /api/properties/{username}, body: " + propertyForm);
        return new ResponseEntity<>(propertyInfo, HttpStatus.CREATED);
    }

    @PutMapping("/{propertyId}")
    @Operation(summary = "Updating property by customer")
    @ApiResponse(responseCode = "200", description = "Property is updated by customer.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<PropertyInfo> update(@PathVariable("propertyId") Long id,
                                               @Valid @RequestBody PropertyForm propertyForm) throws AuthenticationException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, PUT /api/properties/{propertyId} body: " + propertyForm.toString() +
                " with propertyId: " + id);
        PropertyInfo updated = propertyService.update(userDetails.getUsername(), id, propertyForm);
        log.info("PUT data of repository from /api/properties/{propertyId} body: " + propertyForm.toString() +
                " with propertyId: " + id);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PutMapping("/{username}/{propertyId}")
    @Operation(summary = "Updating property by admin")
    @ApiResponse(responseCode = "200", description = "Property is updated by admin.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<PropertyInfo> update(@PathVariable("username") String username, @PathVariable("propertyId") Long id,
                                               @Valid @RequestBody PropertyForm propertyForm) throws AuthenticationException {
        log.info("Http request, PUT /api/properties/{username}/{propertyId}, body: " + propertyForm.toString() +
                " with username: " + username + ",and propertyId" + id);
        PropertyInfo updated = propertyService.update(username, id, propertyForm);
        log.info("PUT data of repository from /api/properties/{username}/{propertyId} body: " + propertyForm +
                " with username: " + username + ",and propertyId" +  + id);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }


    @DeleteMapping("/{propertyId}")
    @Operation(summary = "Deleting property by admin")
    @ApiResponse(responseCode = "200", description = "Property is deleted by admin")
    @Secured({"ROLE_ADMIN"})
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<Void> delete(@PathVariable("propertyId") Long id) {
        log.info("Http request, DELETE /api/properties/{propertyId} with propertyId: " + id);
        propertyService.makeInactive(id);
        log.info("DELETE data of repository from /api/properties/{propertyId} with propertyId: " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/imageurls/{propertyId}")
    @Operation(summary = "Save property's list of image URLs by customer")
    @ApiResponse(responseCode = "201", description = "Property's list of image URLs is saved by customer.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<List<PropertyImageURLInfo>> createListOfImageURLs(@PathVariable("propertyId") Long id, @RequestBody @Valid List<PropertyImageURLForm> propertyImageURLForms) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, POST /api/properties/imageurls/{propertyId}, body: " + propertyImageURLForms.toString());
        List<PropertyImageURLInfo> propertyImageURLInfos = propertyService.createListOfImageURLs(userDetails.getUsername(), id, propertyImageURLForms);
        log.info("POST data of repository from /api/properties/imageurls/{propertyId}, body: " + propertyImageURLForms);
        return new ResponseEntity<>(propertyImageURLInfos, HttpStatus.CREATED);
    }

    @PostMapping("/imageurls/{username}/{propertyId}")
    @Operation(summary = "Save property's list of image URLs by admin")
    @ApiResponse(responseCode = "201", description = "Property's list of image URLs is saved by admin.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<PropertyImageURLInfo>> createListOfImageURLs(@PathVariable("username") String username, @PathVariable("propertyId") Long id, @RequestBody @Valid List<PropertyImageURLForm> propertyImageURLForms) throws AuthenticationExceptionImpl {
        log.info("Http request, POST /api/properties/imageurls/{username}/{propertyId}, body: " + propertyImageURLForms.toString());
        List<PropertyImageURLInfo> propertyImageURLInfos = propertyService.createListOfImageURLs(username, id, propertyImageURLForms);
        log.info("POST data of repository from /api/properties/imageurls/{username}/{propertyId}, body: " + propertyImageURLForms);
        return new ResponseEntity<>(propertyImageURLInfos, HttpStatus.CREATED);
    }

    @GetMapping("/pdf/{propertyId}")
    @Operation(summary = "Getting property with {propertyId} by anybody")
    @ApiResponse(responseCode = "200", description = "Property details wit {propertyId} is got by anybody.")
    public ResponseEntity<byte[]> generatePropertyPDF(@PathVariable("propertyId") Long id) {
        log.info("Http request, GET /api/properties/pdf/{propertyId} with propertyId: " + id);
        propertyService.createPdf(id);
        log.info("GET pdf of repository from /api/properties/pdf/{propertyId} with propertyId: " + id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
