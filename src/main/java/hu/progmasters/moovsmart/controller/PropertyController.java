package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.PropertyDetails;
import hu.progmasters.moovsmart.dto.PropertyForm;
import hu.progmasters.moovsmart.dto.PropertyInfo;
import hu.progmasters.moovsmart.service.PropertyService;
import hu.progmasters.moovsmart.validation.PropertyFormValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@Slf4j
//@Secured({"ROLE_GUEST"})
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
    public ResponseEntity<List<PropertyDetails<Serializable>>> getAllProperties() {
        log.info("Http request, GET /api/list of all properties");
        List<PropertyDetails<Serializable>> propertyList = propertyService.getProperties();
        log.info("GET data from repository/api/list of all properties");
        return new ResponseEntity<>(propertyList, HttpStatus.OK);
    }




    @GetMapping(params = {"page", "size", "sortDir", "sort"})
    public List<PropertyDetails> findPaginated(
             @RequestParam("sortDir") String sortDir,
             @RequestParam("sort") String sort,
             @RequestParam("page") int page,
             @RequestParam("size") int size) {
        List<PropertyDetails> propertyDetailsList = propertyService.getPropertyListPaginated(page, size, sortDir, sort);

        return propertyDetailsList;
    }


    @GetMapping("/{propertyId}")
    @Operation(summary = "Get property with {propertyId}")
    @ApiResponse(responseCode = "200", description = "Property details")
    public ResponseEntity<PropertyDetails<Serializable>> getPropertyDetails(@PathVariable("propertyId") Long id) {
        log.info("Http request, GET /api/property/{propertyId} with variable: " + id);
        PropertyDetails<Serializable> propertyDetails = propertyService.getPropertyDetails(id);
        log.info("GET data from repository/property/{propertyId} with variable: " + id);
        return new ResponseEntity<>(propertyDetails, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Save property")
    @ApiResponse(responseCode = "201", description = "Property saved")
//    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<PropertyInfo> createProperty(@RequestBody @Valid PropertyForm propertyForm) {
        log.info("Http request, POST /api/property, body: " + propertyForm.toString());
        PropertyInfo propertyInfo = propertyService.createProperty(propertyForm);
        log.info("POST data from repository/api/property, body: " + propertyForm);
        return new ResponseEntity<>(propertyInfo, HttpStatus.CREATED);
    }

    @PutMapping("/{propertyId}")
    @Operation(summary = "Update property")
    @ApiResponse(responseCode = "200", description = "Property is updated")
//    @Secured({"ROLE_ADMIN", "ROLE_USER"})
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
//    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> delete(@PathVariable("propertyId") Long id) {
        log.info("Http request, DELETE /api/property/{propertyId} with variable: " + id);
        propertyService.makeInactive(id);
        log.info("DELETE data from repository/api/property/{propertyId} with variable: " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
