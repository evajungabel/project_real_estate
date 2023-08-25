package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.UserForm;
import hu.progmasters.moovsmart.service.UserService;
import hu.progmasters.moovsmart.validation.UserFormValidator;
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
public class UserController {

    private UserService userService;
    private UserFormValidator userFormValidator;

    public UserController(UserService userService, UserFormValidator userFormValidator) {
        this.userService = userService;
        this.userFormValidator = userFormValidator;
    }

    @Autowired


    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody UserForm command) {
        log.info("Http request, POST /api/user, body: " + command.toString());
        userService.save(command);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
