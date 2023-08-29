package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.dto.PropertyDetails;
import hu.progmasters.moovsmart.dto.PropertyForm;
import hu.progmasters.moovsmart.dto.PropertyListItem;
import hu.progmasters.moovsmart.service.PropertyService;
import hu.progmasters.moovsmart.validation.PropertyFormValidator;
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

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<PropertyListItem>> getAllProperties() {
        log.info("Http request, GET /api/list of all properties");
        List<PropertyListItem> propertyList = propertyService.getProperties();
        log.info("GET data from repository/api/list of all properties");
        return new ResponseEntity<>(propertyList, HttpStatus.OK);
    }

    @GetMapping("/{pageNo}/{pageSize}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public List<PropertyListItem> getPaginatedProperties(@PathVariable int pageNo,
                                                         @PathVariable int pageSize) {
        log.info("Http request, GET /api/list of properties with " + pageSize + " number on " + pageNo + " pages");
        List<PropertyListItem> propertyListItems = propertyService.findPaginated(pageNo, pageSize);
        log.info("GET data from repository/api/list of properties with " + pageSize + " number on " + pageNo + " pages");
        return propertyListItems;
    }


    @GetMapping("/{propertyId}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<PropertyDetails> getPropertyDetails(@PathVariable("propertyId") Long id) {
        log.info("Http request, GET /api/property/{propertyId} with variable: " + id);
        PropertyDetails propertyDetails = propertyService.getPropertyDetails(id);
        log.info("GET data from repository/property/{propertyId} with variable: " + id);
        return new ResponseEntity<>(propertyDetails, HttpStatus.OK);
    }



    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<PropertyDetails> getPropertyDetails(@PathVariable("propertyId") Long id) {
        log.info("Http request, GET /api/property/{propertyId} with variable: " + id);
        PropertyDetails propertyDetails = propertyService.getPropertyDetails(id);
        log.info("GET data from repository/property/{propertyId} with variable: " + id);
        return new ResponseEntity<>(propertyDetails, HttpStatus.OK);
    }

    @PostMapping
//    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<PropertyListItem> createProperty(@RequestBody @Valid PropertyForm propertyForm) {
        log.info("Http request, POST /api/property, body: " + propertyForm.toString());
        PropertyListItem propertyListItem = propertyService.createProperty(propertyForm);
        log.info("POST data from repository/api/property, body: " + propertyForm);
        return new ResponseEntity<>(propertyListItem, HttpStatus.CREATED);
    }

    @PutMapping("/{propertyId}")
    public ResponseEntity<PropertyListItem> update(@PathVariable("propertyId") Long id,
                                                   @Valid @RequestBody PropertyForm propertyForm) {
        log.info("Http request, PUT /api/property/{propertyId} body: " + propertyForm.toString() +
                " with variable: " + id);
        PropertyListItem updated = propertyService.update(id, propertyForm);
        log.info("PUT data from repository/api/property/{propertyId} body: " + propertyForm.toString() +
                " with variable: " + id);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    public ResponseEntity<PropertyListItem> createProperty(@RequestBody @Valid PropertyForm propertyForm) {
        log.info("Http request, POST /api/property, body: " + propertyForm.toString());
        PropertyListItem propertyListItem = propertyService.createProperty(propertyForm);
        log.info("POST data from repository/api/property, body: " + propertyForm);
        return new ResponseEntity<>(propertyListItem, HttpStatus.CREATED);
    }

    @PutMapping("/{propertyId}")
    public ResponseEntity<PropertyListItem> update(@PathVariable("propertyId") Long id,
                                          @Valid @RequestBody PropertyForm propertyForm) {
        log.info("Http request, PUT /api/property/{propertyId} body: " + propertyForm.toString() +
                " with variable: " + id);
        PropertyListItem updated = propertyService.update(id, propertyForm);
        log.info("PUT data from repository/api/property/{propertyId} body: " + propertyForm.toString() +
                " with variable: " + id);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }


    @DeleteMapping("/{propertyId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> delete(@PathVariable("propertyId") Long id) {
        log.info("Http request, DELETE /api/property/{propertyId} with variable: " + id);
        propertyService.makeInactive(id);
        log.info("POST data from repository/api/property/{propertyId} with variable: " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }




}
