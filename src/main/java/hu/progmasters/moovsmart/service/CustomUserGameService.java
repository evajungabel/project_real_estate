package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.CustomUserGame;
import hu.progmasters.moovsmart.dto.CustomUserGameForm;
import hu.progmasters.moovsmart.dto.CustomUserGameInfo;
import hu.progmasters.moovsmart.exception.CustomUserPlayedTheGameException;
import hu.progmasters.moovsmart.repository.CustomUserGameRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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


    public CustomUserGameInfo startGame(String username, CustomUserGameForm customUserGameForm, LocalDateTime currentTime) {
        if (verifyConditionForGaming(username, currentTime)) {
            Random generator = new Random();
            CustomUserGame customUserGame = new CustomUserGame().builder()
                    .customUser(customUserService.findCustomUserByUsername(username))
                    .dateOfPlay(LocalDateTime.now())
                    .rouletteNumber(generator.nextInt(2))
                    .build();
            customUserGameRepository.save(customUserGame);

            if (customUserGameForm.getGuessedNumber() != null) {
                guessingNumber(customUserGame, customUserGameForm.getGuessedNumber());
            }

            if (customUserGameForm.getGuessedParity() != null) {
                guessingParity(customUserGame, customUserGameForm.getGuessedParity());
            }

            if (customUserGameForm.getGuessedColour() != null) {
                guessingColour(customUserGame, customUserGameForm.getGuessedColour());
            }

            if (customUserGameForm.getGuessedHalf() != null) {
                guessingHalf(customUserGame, customUserGameForm.getGuessedHalf());
            }


            if (customUserGameForm.getGuessedThirdPart() != null) {
                guessingThirdPart(customUserGame, customUserGameForm.getGuessedThirdPart());
            }

            if (customUserGameForm.getGuessedDividedByThree() != null) {
                guessingDividedByThree(customUserGame, customUserGameForm.getGuessedDividedByThree());
            }

            CustomUserGameInfo customUserGameInfo = modelMapper.map(customUserGame, CustomUserGameInfo.class);
            customUserGameInfo.setCustomUserUsername(username);
            return customUserGameInfo;
        } else {
            throw new CustomUserPlayedTheGameException(username);
        }
    }

    public CustomUserGame guessingNumber(CustomUserGame customUserGame, Integer guessedNumber) {
        customUserGame.setGuessedNumber(guessedNumber);
        if (customUserGame.getRouletteNumber().equals(customUserGame.getGuessedNumber())) {
            customUserGame.setResultMessage("Congratulate! You win!");
        } else {
            customUserGame.setResultMessage("You didn't win for now, but try for the next time!");
        }
        return customUserGame;
    }

    public CustomUserGame guessingParity(CustomUserGame customUserGame, Integer guessedParity) {
        customUserGame.setGuessedParity(guessedParity);
        if (customUserGame.getRouletteNumber() % 2 == customUserGame.getGuessedParity()) {
            customUserGame.setResultMessage("Congratulate! You win!");
        } else {
            customUserGame.setResultMessage("You didn't win for now, but try for the next time!");
        }
        return customUserGame;
    }

    public CustomUserGame guessingHalf(CustomUserGame customUserGame, Integer guessedHalf) {
        customUserGame.setGuessedHalf(guessedHalf);
        if ((customUserGame.getRouletteNumber() <= 18 && customUserGame.getGuessedHalf() == 0)
        || (19 <= customUserGame.getRouletteNumber() && customUserGame.getGuessedHalf() == 1)) {
            customUserGame.setResultMessage("Congratulate! You win!");
        } else {
            customUserGame.setResultMessage("You didn't win for now, but try for the next time!");
        }
        return customUserGame;
    }

    public CustomUserGame guessingColour(CustomUserGame customUserGame, Integer guessedColour) {
        customUserGame.setGuessedColour(guessedColour);
        if ((customUserGame.getRouletteNumber() % 2 == customUserGame.getGuessedColour() && customUserGame.getRouletteNumber() <= 9)
        || (customUserGame.getRouletteNumber() % 2 == customUserGame.getGuessedColour() && 12 <= customUserGame.getRouletteNumber() && customUserGame.getRouletteNumber() <= 18)
                || (customUserGame.getRouletteNumber() % 2 == customUserGame.getGuessedColour() && 21 <= customUserGame.getRouletteNumber() && customUserGame.getRouletteNumber() <= 27)
                || (customUserGame.getRouletteNumber() % 2 == customUserGame.getGuessedColour() && 28 <= customUserGame.getRouletteNumber())) {
            customUserGame.setResultMessage("Congratulate! You win!");
        } else {
            customUserGame.setResultMessage("You didn't win for now, but try for the next time!");
        }
        return customUserGame;
    }

    public CustomUserGame guessingThirdPart(CustomUserGame customUserGame, Integer guessedThirdPart) {
        customUserGame.setGuessedThirdPart(guessedThirdPart);
        if ((customUserGame.getGuessedThirdPart() == 1 && customUserGame.getRouletteNumber() <= 9)
                || (customUserGame.getGuessedThirdPart() == 2 && 10 <= customUserGame.getRouletteNumber() && customUserGame.getRouletteNumber() <= 18)
                || (customUserGame.getGuessedThirdPart() == 3 && 19 <= customUserGame.getRouletteNumber())) {
            customUserGame.setResultMessage("Congratulate! You win!");
        } else {
            customUserGame.setResultMessage("You didn't win for now, but try for the next time!");
        }
        return customUserGame;
    }


    public CustomUserGame guessingDividedByThree(CustomUserGame customUserGame, Integer guessedDvidedByThree) {
        customUserGame.setGuessedDividedByThree(guessedDvidedByThree);
        if ((customUserGame.getGuessedThirdPart() == 0 && customUserGame.getRouletteNumber() % 3 == 0)
                || (customUserGame.getGuessedThirdPart() == 1 && customUserGame.getRouletteNumber() % 3 == 1)
                || (customUserGame.getGuessedThirdPart() == 2 && customUserGame.getRouletteNumber() % 3 == 2)) {
            customUserGame.setResultMessage("Congratulate! You win!");
        } else {
            customUserGame.setResultMessage("You didn't win for now, but try for the next time!");
        }
        return customUserGame;
    }

    public boolean verifyConditionForGaming(String username, LocalDateTime currentTime) {
        int count = 0;
        for (CustomUserGame customUserGame : customUserService.findCustomUserByUsername(username).getCustomUserGames()) {
            if (customUserGame.getDateOfPlay().toLocalDate().isEqual(currentTime.toLocalDate())) {
                count++;
            }
        }
        return count == 0;
    }



}
