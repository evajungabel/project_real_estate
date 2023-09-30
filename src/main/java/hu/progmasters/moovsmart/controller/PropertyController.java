package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.domain.Currencies;
import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.AuthenticationExceptionImpl;
import hu.progmasters.moovsmart.service.PropertyService;
import hu.progmasters.moovsmart.validation.PropertyDataFormYearValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Currency;
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
    @Operation(summary = "Get all properties")
    @ApiResponse(responseCode = "200", description = "List of properties")
    public ResponseEntity<List<PropertyDetails>> getAllProperties() {
        log.info("Http request, GET /api/list of all properties");
        List<PropertyDetails> propertyList = propertyService.getProperties();
        log.info("GET data from repository/api/list of all properties");
        return new ResponseEntity<>(propertyList, HttpStatus.OK);
    }


    @GetMapping(params = {"page", "size", "sortDir", "sort"})
    @Operation(summary = "Get list of paginated and sorted property")
    @ApiResponse(responseCode = "200", description = "Paginated and sorted list of property is got.")
    public ResponseEntity<List<PropertyDetails>> findPaginatedAndSorted(
            @RequestParam("sortDir") String sortDir,
            @RequestParam("sort") String sort,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        log.info("Http request, GET /api/property with variables: " + page + size + sort + sortDir);
        List<PropertyDetails> propertyDetailsList = propertyService.getPropertyListPaginatedAndSorted(page, size, sortDir, sort);
        log.info("GET data from repository/property with variable: " + page + size + sort + sortDir);
        return new ResponseEntity<>(propertyDetailsList, HttpStatus.OK);
    }


    @PostMapping("/requests")
    @Operation(summary = "Get filtered, paginated and sorted list of property")
    @ApiResponse(responseCode = "200", description = "Paginated, sorted and filtered list of property is got.")
    public ResponseEntity<List<PropertyFilterRequestInfo>> findByRequestedValues(
            @RequestParam("sortDir") String sortDir,
            @RequestParam("sort") String sort,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @Valid @RequestBody PropertyFilterRequestForm propertyFilterRequestForm) {
        log.info("Http request, POST /api/property with variables: " + page + size + sort + sortDir + propertyFilterRequestForm);
        List<PropertyFilterRequestInfo> propertyFilterRequestInfos = propertyService.getPropertyRequests(page, size, sortDir, sort, propertyFilterRequestForm);
        log.info("POST data from repository/property with variable: " + page + size + sort + sortDir + propertyFilterRequestForm);
        return new ResponseEntity<>(propertyFilterRequestInfos, HttpStatus.OK);
    }

    @GetMapping("/{propertyId}")
    @Operation(summary = "Get property with {propertyId}")
    @ApiResponse(responseCode = "200", description = "Property details")
    public ResponseEntity<PropertyDetails> getPropertyDetails(@PathVariable("propertyId") Long id) {
        log.info("Http request, GET /api/property/{propertyId} with variable: " + id);
        PropertyDetails propertyDetails = propertyService.getPropertyDetails(id);
        log.info("GET data from repository/property/{propertyId} with variable: " + id);
        return new ResponseEntity<>(propertyDetails, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Save property")
    @ApiResponse(responseCode = "201", description = "Property saved")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<PropertyInfo> createProperty(@RequestBody @Valid PropertyForm propertyForm) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, POST /api/property, body: " + propertyForm.toString());
        PropertyInfo propertyInfo = propertyService.createProperty(userDetails.getUsername(), propertyForm);
        log.info("POST data from repository/api/property, body: " + propertyForm);
        return new ResponseEntity<>(propertyInfo, HttpStatus.CREATED);
    }

    @PostMapping("/{username}")
    @Operation(summary = "Save property")
    @ApiResponse(responseCode = "201", description = "Property saved")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<PropertyInfo> createProperty(@PathVariable("username") String username, @RequestBody @Valid PropertyForm propertyForm) {
        log.info("Http request, POST /api/property, body: " + propertyForm.toString());
        PropertyInfo propertyInfo = propertyService.createProperty(username, propertyForm);
        log.info("POST data from repository/api/property, body: " + propertyForm);
        return new ResponseEntity<>(propertyInfo, HttpStatus.CREATED);
    }

    @PutMapping("/{propertyId}")
    @Operation(summary = "Update property")
    @ApiResponse(responseCode = "200", description = "Property is updated")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<PropertyInfo> update(@PathVariable("propertyId") Long id,
                                               @Valid @RequestBody PropertyForm propertyForm) throws AuthenticationException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, PUT /api/property/{propertyId} body: " + propertyForm.toString() +
                " with variable: " + id);
        PropertyInfo updated = propertyService.update(userDetails.getUsername(), id, propertyForm);
        log.info("PUT data from repository/api/property/{propertyId} body: " + propertyForm.toString() +
                " with variable: " + id);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PutMapping("/{username}/{propertyId}")
    @Operation(summary = "Update property")
    @ApiResponse(responseCode = "200", description = "Property is updated")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<PropertyInfo> update(@PathVariable("username") String username, @PathVariable("propertyId") Long id,
                                               @Valid @RequestBody PropertyForm propertyForm) throws AuthenticationException {
        log.info("Http request, PUT /api/property/{propertyId} body: " + propertyForm.toString() +
                " with variable: " + id);
        PropertyInfo updated = propertyService.update(username, id, propertyForm);
        log.info("PUT data from repository/api/property/{propertyId} body: " + propertyForm.toString() +
                " with variable: " + id);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }


    @DeleteMapping("/{propertyId}")
    @Operation(summary = "Delete property")
    @ApiResponse(responseCode = "200", description = "Property is deleted")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> delete(@PathVariable("propertyId") Long id) {
        log.info("Http request, DELETE /api/property/{propertyId} with variable: " + id);
        propertyService.makeInactive(id);
        log.info("DELETE data from repository/api/property/{propertyId} with variable: " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/imageurls/{propertyId}")
    @Operation(summary = "Save property's list of image URLs")
    @ApiResponse(responseCode = "201", description = "Property's list of image URLs is saved")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<List<PropertyImageURLInfo>> createListOfImageURLs(@PathVariable("propertyId") Long id, @RequestBody @Valid List<PropertyImageURLForm> propertyImageURLForms) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, POST /api/property/imageurls, body: " + propertyImageURLForms.toString());
        List<PropertyImageURLInfo> propertyImageURLInfos = propertyService.createListOfImageURLs(userDetails.getUsername(), id, propertyImageURLForms);
        log.info("POST data from repository/api/property/imageurls, body: " + propertyImageURLForms);
        return new ResponseEntity<>(propertyImageURLInfos, HttpStatus.CREATED);
    }

    @PostMapping("/imageurls/{username}/{propertyId}")
    @Operation(summary = "Save property's list of image URLs")
    @ApiResponse(responseCode = "201", description = "Property's list of image URLs is saved")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<PropertyImageURLInfo>> createListOfImageURLs(@PathVariable("username") String username, @PathVariable("propertyId") Long id, @RequestBody @Valid List<PropertyImageURLForm> propertyImageURLForms) throws AuthenticationExceptionImpl {
        log.info("Http request, POST /api/property/imageurls, body: " + propertyImageURLForms.toString());
        List<PropertyImageURLInfo> propertyImageURLInfos = propertyService.createListOfImageURLs(username, id, propertyImageURLForms);
        log.info("POST data from repository/api/property/imageurls, body: " + propertyImageURLForms);
        return new ResponseEntity<>(propertyImageURLInfos, HttpStatus.CREATED);
    }

    @GetMapping("/pdf/{propertyId}")
    @Operation(summary = "Create PDF file")
    @ApiResponse(responseCode = "201", description = "Property details")
    public ResponseEntity<byte[]> generatePropertyPDF(@PathVariable("propertyId") Long id) {
        log.info("Http request, GET /api/property/pdf/{propertyId} with variable: " + id);
        propertyService.createPdf(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/exchange/{propertyId}/{currency}")
    @Operation(summary = "Exchange property price")
    @ApiResponse(responseCode = "201", description = "Property details")
    public ResponseEntity<Double> changePrice(@PathVariable("propertyId") Long id, @PathVariable("currency") Currencies currency) {
        log.info("Http request, GET /api/property/exchange/{propertyId} with variable: " + id);
        Double changedPrice = propertyService.exchange(id,currency);
        return new ResponseEntity<>(changedPrice,HttpStatus.CREATED);
    }
}
