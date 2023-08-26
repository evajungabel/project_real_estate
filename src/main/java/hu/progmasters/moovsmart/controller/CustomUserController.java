package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.CustomUserForm;
import hu.progmasters.moovsmart.service.CustomUserService;
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
@RequestMapping("/api/user")
@Slf4j
public class CustomUserController {

    private CustomUserService customUserService;


    @Autowired
    public CustomUserController(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }




    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CustomUserForm command) {
        log.info("Http request, POST /api/user, body: " + command.toString());
        customUserService.save(command);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
