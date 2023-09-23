package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.service.PropertyService;
import hu.progmasters.moovsmart.validation.PropertyFormValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@Slf4j
public class PropertyController {

    private PropertyService propertyService;
    private PropertyFormValidator propertyFormValidator;

    @Autowired
    public PropertyController(PropertyService propertyService, PropertyFormValidator propertyFormValidator) {
        this.propertyService = propertyService;
        this.propertyFormValidator = propertyFormValidator;
    }

    @InitBinder("propertyDetails")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(propertyFormValidator);
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
    public ResponseEntity<List<PropertyDetails>> findPaginated(
             @RequestParam("sortDir") String sortDir,
             @RequestParam("sort") String sort,
             @RequestParam("page") int page,
             @RequestParam("size") int size) {
        log.info("Http request, GET /api/property with variables: " + page + size + sort + sortDir);
        List<PropertyDetails> propertyDetailsList = propertyService.getPropertyListPaginated(page, size, sortDir, sort);
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
            @Valid @RequestBody PropertyFilterRequestForm propertyFilterRequestForm
    ) {
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

        log.info("Http request, POST /api/property, body: " + propertyForm.toString());
        PropertyInfo propertyInfo = propertyService.createProperty(propertyForm);
        log.info("POST data from repository/api/property, body: " + propertyForm);
        return new ResponseEntity<>(propertyInfo, HttpStatus.CREATED);
    }

    @PutMapping("/{propertyId}")
    @Operation(summary = "Update property")
    @ApiResponse(responseCode = "200", description = "Property is updated")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<PropertyInfo> update(@PathVariable("propertyId") Long id,
                                               @Valid @RequestBody PropertyForm propertyForm) {
        log.info("Http request, PUT /api/property/{propertyId} body: " + propertyForm.toString() +
                " with variable: " + id);
        PropertyInfo updated = propertyService.update(id, propertyForm);
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
    public ResponseEntity<List<PropertyImageURLInfo>> createListOfImageURLs(@PathVariable("propertyId") Long id, @RequestBody @Valid List<PropertyImageURLForm> propertyImageURLForms) {
        log.info("Http request, POST /api/property/imageurls, body: " + propertyImageURLForms.toString());
        List<PropertyImageURLInfo> propertyImageURLInfos = propertyService.createListOfImageURLs(id, propertyImageURLForms);
        log.info("POST data from repository/api/property/imageurls, body: " + propertyImageURLForms);
        return new ResponseEntity<>(propertyImageURLInfos, HttpStatus.CREATED);
    }

}
