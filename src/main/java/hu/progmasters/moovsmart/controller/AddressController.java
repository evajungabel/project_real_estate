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
    @ApiResponse(responseCode = "201", description = "address saved")
    public ResponseEntity<Void> createAddress(@Valid @RequestBody AddressForm form) {
        log.info("Http request, GET /api/addresses" + form.toString());
        addressService.saveAddress(form);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //TODO ListAll

//    @GetMapping("/id/{id}")
//    @Operation(summary = "Find addres by id.")
//    public ResponseEntity<AddressInfo> findById(@PathVariable("id") Long id) {
//        log.info("Http request, GET /api/addresses/{id} with variable" + id);
//        AddressInfo addressInfo = addressService.findById(id);
//        return new ResponseEntity<>(addressInfo, HttpStatus.OK);
//    }

    //TODO update


    //TODO delete


    //TODO FindByValue
}
