package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.AddressForm;
import hu.progmasters.moovsmart.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Void> createAddress(@Valid @RequestBody AddressForm form) {
        log.info("Http request, GET /api/addresses" + form.toString());
        addressService.saveAddress(form);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //TODO ListAll


    //TODO FindById


    //TODO update



    //TODO delete


    //TODO FindByValue
}
