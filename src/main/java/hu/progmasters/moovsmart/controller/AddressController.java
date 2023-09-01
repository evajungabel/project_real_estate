package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.AddressForm;
import hu.progmasters.moovsmart.dto.AddressInfo;
import hu.progmasters.moovsmart.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/addresses")
@Slf4j
public class AddressController {
    private AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    @Operation(summary = "Save address")
    @ApiResponse(responseCode = "201", description = "Save Address")
    public ResponseEntity<Void> createAddress(@Valid @RequestBody AddressForm form) {
        log.info("Http request, GET /api/addresses" + form.toString());
        addressService.saveAddress(form);
        log.info("POST data from repository/api/addresses, body: " + form);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Find addres by id.")
    @ApiResponse(responseCode = "200", description = "List of Addresses")
    public ResponseEntity<AddressInfo> findById(@PathVariable("id") Long id) {
        log.info("Http request, GET /api/addresses/{id} with variable" + id);
        AddressInfo addressInfo = addressService.findById(id);
        log.info("GET data from repository/api/list of all addresses");
        return new ResponseEntity<>(addressInfo, HttpStatus.OK);
    }

    @PutMapping("id/{id}")
    @Operation(summary = "update address")
    @ApiResponse(responseCode = "200", description = "Update Addresses")
    public ResponseEntity<AddressInfo> update(@PathVariable("id") Long id, @Valid @RequestBody AddressForm form){
        log.info("Http request, PUT /api/bee/{beeId} body: " + form.toString() + " With variable: " + id);
        AddressInfo update = addressService.update(id, form);
        log.info("PUT data from repository/api/update address");
        return new ResponseEntity<>(update,HttpStatus.OK);
    }

    @DeleteMapping("/id/{id}")
    @Operation(summary = "Delete address")
    @ApiResponse(responseCode = "200", description = "Delete Addresses")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        log.info("Http request, DELETE /api/addresses/{id} with variable: " + id);
        addressService.delete(id);
        log.info("DELETE data from repository/api/delete address whit id: " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/value/{value}")
    @Operation(summary = "Find address by value.")
    @ApiResponse(responseCode = "200", description = "Find Addresses")
    public ResponseEntity<AddressInfo> findByValue(@PathVariable("value") String value) {
        log.info("Http request, GET /api/addresses/{value} with variable" + value);
        AddressInfo addressInfo = addressService.findByValue(value);
        log.info("GET data from repository/api/list of all addresses");
        return new ResponseEntity<>(addressInfo, HttpStatus.OK);
    }
}
