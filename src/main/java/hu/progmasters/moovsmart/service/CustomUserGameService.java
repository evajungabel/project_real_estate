package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.CustomUserGame;
import hu.progmasters.moovsmart.dto.CustomUserGameInfo;
import hu.progmasters.moovsmart.repository.CustomUserGameRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Random;

@Service
@Transactional
public class CustomUserGameService {

    private final CustomUserGameRepository customUserGameRepository;
    private ModelMapper modelMapper;

    private CustomUserService customUserService;

    @Autowired
    public CustomUserGameService(CustomUserGameRepository customUserGameRepository, ModelMapper modelMapper, CustomUserService customUserService) {
        this.customUserGameRepository = customUserGameRepository;
        this.modelMapper = modelMapper;
        this.customUserService = customUserService;
    }



//    @Scheduled(cron = "0 */5 * ? * *")

    scheduler.schedule(task, new CronTrigger("0 15 9-17 * * MON-FRI"));

    public CustomUserGameInfo startGame(String username, Integer guessedNumber) {
        Random generator = new Random();
        CustomUserGame customUserGame = new CustomUserGame().builder()
                .customUser(customUserService.findCustomUserByUsername(username))
                .dateOfPlay(LocalDate.now())
                .guessedNumber(guessedNumber)
                .rouletteNumber(generator.nextInt(2))
                .build();
        customUserGameRepository.save(customUserGame);
        if (customUserGame.getRouletteNumber().equals(customUserGame.getGuessedNumber())) {
            customUserGame.setResultMessage("Congratulate! You win!");
        } else {
            customUserGame.setResultMessage("You didn't win for now, but try for the next time!");
        }
        CustomUserGameInfo customUserGameInfo = modelMapper.map(customUserGame, CustomUserGameInfo.class);
        customUserGameInfo.setCustomUserUsername(username);
        return customUserGameInfo;
    }
}
