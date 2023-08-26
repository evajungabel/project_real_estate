package hu.progmasters.moovsmart.controller;

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
        return new ResponseEntity<>(propertyService.getProperties(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<PropertyDetails> getPropertyDetails(@PathVariable("id") Long id) {
        return new ResponseEntity<>(propertyService.getPropertyDetails(id), HttpStatus.OK);
    }

    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Void> createProperty(@RequestBody @Valid PropertyForm propertyForm) {
        propertyService.createProperty(propertyForm);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{propertyId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> delete(@PathVariable("propertyId") Long id) {
        log.info("Http request, DELETE /api/property/{propertyId} with variable: " + id);
        propertyService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }




}
