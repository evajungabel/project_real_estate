package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.domain.CustomUserGame;
import hu.progmasters.moovsmart.dto.CustomUserForm;
import hu.progmasters.moovsmart.dto.CustomUserGameForm;
import hu.progmasters.moovsmart.dto.CustomUserGameInfo;
import hu.progmasters.moovsmart.dto.CustomUserInfo;
import hu.progmasters.moovsmart.service.CustomUserGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/game")
@Slf4j
public class CustomUserGameController {

    private CustomUserGameService customUserGameService;

    @Autowired
    public CustomUserGameController(CustomUserGameService customUserGameService) {
        this.customUserGameService = customUserGameService;
    }


    @PostMapping()
    @Operation(summary = "Customer game")
    @ApiResponse(responseCode = "201", description = "Customer is played and saved")
    public ResponseEntity<CustomUserGameInfo> register(@RequestParam("guessedNumber") Integer guessedNumber) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info("Http request, POST /api/game, body: " + customUserGameForm.toString());
        CustomUserGameInfo customUserGameInfo = customUserGameService.startGame(userDetails.getUsername(), guessedNumber);
//        log.info("POST data from repository/api/game, body: " + customUserGameForm);
        return new ResponseEntity<>(customUserGameInfo, HttpStatus.CREATED);
    }

}
