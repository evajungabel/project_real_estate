package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.dto.CustomUserGameForm;
import hu.progmasters.moovsmart.dto.CustomUserGameInfo;
import hu.progmasters.moovsmart.exception.CustomUserPlayedTheGameException;
import hu.progmasters.moovsmart.exception.ThisIsNotAGameDayException;
import hu.progmasters.moovsmart.service.CustomUserGameService;
import hu.progmasters.moovsmart.service.CustomUserService;
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

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/game")
@Slf4j
public class CustomUserGameController {

    private CustomUserGameService customUserGameService;

    private CustomUserService customUserService;

    @Autowired
    public CustomUserGameController(CustomUserGameService customUserGameService, CustomUserService customUserService) {
        this.customUserGameService = customUserGameService;
        this.customUserService = customUserService;
    }


    @PostMapping()
    @Operation(summary = "Customer game")
    @ApiResponse(responseCode = "201", description = "Customer is played and saved")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<CustomUserGameInfo> startGame(@RequestBody CustomUserGameForm customUserGameForm) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDateTime currentTime = LocalDateTime.now();
         if (currentTime.getDayOfWeek() == DayOfWeek.TUESDAY) {
                log.info("Http request, POST /api/game, username: " + userDetails.getUsername());
                CustomUserGameInfo customUserGameInfo = customUserGameService.startGame(userDetails.getUsername(), customUserGameForm, currentTime);
                log.info("POST data from repository/api/game, username: " + userDetails.getUsername());
                return new ResponseEntity<>(customUserGameInfo, HttpStatus.CREATED);
            } else {
             throw new ThisIsNotAGameDayException(currentTime);
         }
    }

}
