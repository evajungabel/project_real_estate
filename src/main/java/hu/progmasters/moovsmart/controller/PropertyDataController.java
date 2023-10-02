package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.PropertyDataForm;
import hu.progmasters.moovsmart.dto.PropertyDataInfo;
import hu.progmasters.moovsmart.exception.AuthenticationExceptionImpl;
import hu.progmasters.moovsmart.service.PropertyDataService;
import hu.progmasters.moovsmart.validation.PropertyDataFormYearValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/properties/data")
@Slf4j
public class PropertyDataController {
    private final PropertyDataService propertyDataService;

    private PropertyDataFormYearValidator propertyDataFormYearValidator;

    @Autowired
    public PropertyDataController(PropertyDataService propertyDataService, PropertyDataFormYearValidator propertyDataFormYearValidator) {
        this.propertyDataService = propertyDataService;
        this.propertyDataFormYearValidator = propertyDataFormYearValidator;
    }


    @GetMapping("/{propertyId}")
    @Operation(summary = "Get property data for property id {propertyId} by anybody")
    @ApiResponse(responseCode = "200", description = "Property data is got by anybody.")
    public ResponseEntity<PropertyDataInfo> getPropertyData(@PathVariable("propertyId") Long id) {
        log.info("Http request, GET /api/properties/data/{propertyId}, with propertyId" + id);
        PropertyDataInfo propertyDataInfo = propertyDataService.getPropertyData(id);
        log.info("GET data of repository from /api/properties/data/{propertyId}, with propertyId: " + id);
        return new ResponseEntity<>(propertyDataInfo, HttpStatus.OK);
    }


    @GetMapping()
    @Operation(summary = "Getting list of paginated and sorted property data by anybody")
    @ApiResponse(responseCode = "200", description = "Paginated and sorted list of property data is got by anybody.")
    public ResponseEntity<List<PropertyDataInfo>> findPaginated(
            @RequestParam("sortDir") String sortDir,
            @RequestParam("sort") String sort,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        log.info("Http request, GET /api/properties/data, with variables: " + page + size + sort + sortDir);
        List<PropertyDataInfo> propertyDataInfos = propertyDataService.getPropertyDataListPaginated(page, size, sortDir, sort);
        log.info("GET data of repository from api/properties/data, with variable: " + page + size + sort + sortDir);
        return new ResponseEntity<>(propertyDataInfos, HttpStatus.OK);
    }

    @PostMapping("/{propertyId}")
    @Operation(summary = "Saving property data for property id {propertyId} by customer")
    @ApiResponse(responseCode = "201", description = "Property data for property id {propertyId} is saved by customer.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<PropertyDataInfo> createPropertyData(@PathVariable("propertyId") Long propertyId,
                                                               @RequestBody @Valid PropertyDataForm propertyDataForm) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, POST /api/properties/data/{propertyId}, with propertyId" + propertyId + " with body: " + propertyDataForm.toString());
        PropertyDataInfo propertyDataInfo = propertyDataService.createPropertyData(userDetails.getUsername(), propertyDataForm, propertyId);
        log.info("POST data of repository from /api/properties/data/{propertyId}, with propertyId" + propertyId + " with body: " + propertyDataForm);
        return new ResponseEntity<>(propertyDataInfo, HttpStatus.CREATED);
    }

    @PostMapping("/{username}/{propertyId}")
    @Operation(summary = "Saving property data for property id {propertyId} by admin")
    @ApiResponse(responseCode = "201", description = "Property data for property id {propertyId} is saved by admin.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<PropertyDataInfo> createPropertyData(@PathVariable("propertyId") Long propertyId,
                                                               @PathVariable("username") String username,
                                                               @RequestBody @Valid PropertyDataForm propertyDataForm) throws AuthenticationExceptionImpl {
        log.info("Http request, POST /api/properties/data/{username}/{propertyId}, with propertyId: " + propertyId + " with body: " + propertyDataForm.toString());
        PropertyDataInfo propertyDataInfo = propertyDataService.createPropertyData(username, propertyDataForm, propertyId);
        log.info("POST data of repository from /api/properties/data/{username}/{propertyId}, with propertyId: " + propertyId + " with body: " + propertyDataForm);
        return new ResponseEntity<>(propertyDataInfo, HttpStatus.CREATED);
    }

    @PutMapping("/{propertyId}")
    @Operation(summary = "Updating property data for property id {propertyId} by customer")
    @ApiResponse(responseCode = "200", description = "Property data for property id {propertyId} is updated by customer.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<PropertyDataInfo> updatePropertyData(@PathVariable("propertyId") Long id,
                                                               @Valid @RequestBody PropertyDataForm propertyDataForm) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, PUT /api/properties/data/{propertyId}, with protpertyId: " + id + " and body: " + propertyDataForm.toString());
        PropertyDataInfo updated = propertyDataService.update(userDetails.getUsername(), propertyDataForm, id);
        log.info("PUT data of repository from /api/properties/data/{propertyId}, with protpertyId: " + id + " and body: " + propertyDataForm.toString());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PutMapping("/{username}/{propertyId}")
    @Operation(summary = "Update property data for property id {propertyId} by admin")
    @ApiResponse(responseCode = "200", description = "Property data for property id {propertyId} is updated by admin.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<PropertyDataInfo> updatePropertyData(@PathVariable("propertyId") Long id,
                                                               @PathVariable("username") String username,
                                                               @Valid @RequestBody PropertyDataForm propertyDataForm) throws AuthenticationExceptionImpl {
        log.info("Http request, PUT /api/properties/data/{username}/{propertyId}, with username: " + username + "and with protpertyId: " + id + " and body: " + propertyDataForm.toString());
        PropertyDataInfo updated = propertyDataService.update(username, propertyDataForm, id);
        log.info("PUT data of repository from /api/properties/data/{username}/{propertyId}, with username: " + username + "and with protpertyId: " + id + " and body: " + propertyDataForm.toString());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{propertyId}")
    @Operation(summary = "Delete property data for property id {propertyId} by customer")
    @ApiResponse(responseCode = "200", description = "Property data for property id {propertyId} is deleted by customer.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<Void> deletePropertyData(@PathVariable("propertyId") Long id) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, DELETE /api/properties/data/{propertyId}, with propertyId: " + id);
        propertyDataService.deleteByPropertyId(userDetails.getUsername(), id);
        log.info("DELETE data of repository from /api/properties/data/{propertyId}, with propertyId " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{username}/{propertyId}")
    @Operation(summary = "Delete property data for property id {propertyId} by admin")
    @ApiResponse(responseCode = "200", description = "Property data for property id {propertyId} is deleted by admin.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> deletePropertyData(@PathVariable("username") String username, @PathVariable("propertyId") Long id) throws AuthenticationExceptionImpl {
        log.info("Http request, DELETE /api/properties/data/{propertyId} with propertyId: " + id);
        propertyDataService.deleteByPropertyId(username, id);
        log.info("DELETE deleted data from repository propertyData for propertyId " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
