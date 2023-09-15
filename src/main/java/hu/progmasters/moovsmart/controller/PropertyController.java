package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.domain.SimplePage;
import hu.progmasters.moovsmart.dto.PropertyDetails;
import hu.progmasters.moovsmart.dto.PropertyForm;
import hu.progmasters.moovsmart.dto.PropertyInfo;
import hu.progmasters.moovsmart.service.PropertyService;
import hu.progmasters.moovsmart.validation.PropertyFormValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<PropertyDetails>> getAllProperties() {
        log.info("Http request, GET /api/list of all properties");
        List<PropertyDetails> propertyList = propertyService.getProperties();
        log.info("GET data from repository/api/list of all properties");
        return new ResponseEntity<>(propertyList, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<SimplePage<PropertyDetails>> getAllRoles(
            @SortDefault(sort = "price") @PageableDefault(size = 10) final Pageable pageable) {
        return ResponseEntity.ok(propertyService.getPropertyListPaginated(pageable));
    }



//    @GetMapping
//    @ResponseBody
//    public List<PropertyDetails> getPropertyListPaginated(
//            @PathVariable("page") int page,
//            @PathVariable("size") int size,
//            @PathVariable("sortDir") String sortDir,
//            @PathVariable("sort") String sort) {
//
//        List<PropertyDetails> propertyDetailsList = propertyService.getPropertyListPaginated(page, size, sortDir, sort);
//        return propertyDetailsList;
//    }

//    @GetMapping(params = { "page", "size" })
//    public List<PropertyDetails> findPaginated(@RequestParam("page") int page,
//                                   @RequestParam("size") int size, UriComponentsBuilder uriBuilder,
//                                   HttpServletResponse response) {
//        Page<PropertyDetails> resultPage = (Page<PropertyDetails>) propertyService.findPaginated(page, size);
//        if (page > resultPage.getTotalPages()) {
//            throw new MyResourceNotFoundException();
//        }
//        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<PropertyDetails>(
//                PropertyDetails.class, uriBuilder, response, page, resultPage.getTotalPages(), size));
//
//        return resultPage.getContent();
//    }

//    @GetMapping("/{pageNo}/{pageSize}")
//    @Operation(summary = "Get properties on one page {pageSize} number")
//    @ApiResponse(responseCode = "200", description = "Property saved")
//    public List<PropertyInfo> getPaginatedProperties(@PathVariable int pageNo,
//                                                     @PathVariable int pageSize) {
//        log.info("Http request, GET /api/list of properties with " + pageSize + " number on " + pageNo + " pages");
//        List<PropertyInfo> propertyInfos = propertyService.findPaginated(pageNo, pageSize);
//        log.info("GET data from repository/api/list of properties with " + pageSize + " number on " + pageNo + " pages");
//        return propertyInfos;
//    }

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
