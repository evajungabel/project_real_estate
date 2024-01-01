package com.realestate.service;

import com.realestate.dto.CustomUserGameInfo;
import com.realestate.domain.CustomUserGame;
import com.realestate.dto.CustomUserGameForm;
import com.realestate.exception.CustomUserHasNotRightNumberOfInputsForTheGameException;
import com.realestate.exception.CustomUserPlayedTheGameException;
import com.realestate.repository.CustomUserGameRepository;
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

    private final String congratulate = "Congratulate! You win!";
    private final String loss = "You didn't win for now, but try for the next time!";

    @Autowired
    public CustomUserGameService(CustomUserGameRepository customUserGameRepository, ModelMapper modelMapper, CustomUserService customUserService) {
        this.customUserGameRepository = customUserGameRepository;
        this.modelMapper = modelMapper;
        this.customUserService = customUserService;
    }


    public CustomUserGameInfo startGame(String username, CustomUserGameForm customUserGameForm, LocalDateTime currentTime) {
        if (verifyConditionOfTriesForGaming(username, currentTime)) {
            if (verifyConditionOfInputsForGaming(customUserGameForm)) {
                Random generator = new Random();
                CustomUserGame customUserGame = new CustomUserGame().builder()
                        .customUser(customUserService.findCustomUserByUsername(username))
                        .dateOfPlay(LocalDateTime.now())
                        .rouletteNumber(generator.nextInt(37))
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
                throw new CustomUserHasNotRightNumberOfInputsForTheGameException(username);
            }
        } else {
            throw new CustomUserPlayedTheGameException(username);
        }
    }

    public CustomUserGame guessingNumber(CustomUserGame customUserGame, Integer guessedNumber) {
        customUserGame.setGuessedNumber(guessedNumber);
        if (customUserGame.getRouletteNumber().equals(customUserGame.getGuessedNumber())) {
            customUserGame.setResultMessage(congratulate);
        } else {
            customUserGame.setResultMessage(loss);
        }
        return customUserGame;
    }

    public CustomUserGame guessingParity(CustomUserGame customUserGame, Integer guessedParity) {
        customUserGame.setGuessedParity(guessedParity);
        if (customUserGame.getRouletteNumber() % 2 == customUserGame.getGuessedParity()) {
            customUserGame.setResultMessage(congratulate);
        } else {
            customUserGame.setResultMessage(loss);
        }
        return customUserGame;
    }

    public CustomUserGame guessingHalf(CustomUserGame customUserGame, Integer guessedHalf) {
        customUserGame.setGuessedHalf(guessedHalf);
        if ((customUserGame.getRouletteNumber() <= 18 && customUserGame.getGuessedHalf() == 1)
                || (19 <= customUserGame.getRouletteNumber() && customUserGame.getGuessedHalf() == 2)) {
            customUserGame.setResultMessage(congratulate);
        } else {
            customUserGame.setResultMessage(loss);
        }
        return customUserGame;
    }

    public CustomUserGame guessingColour(CustomUserGame customUserGame, Integer guessedColour) {
        customUserGame.setGuessedColour(guessedColour);
        if ((customUserGame.getRouletteNumber() % 2 != customUserGame.getGuessedColour() &&
                (customUserGame.getRouletteNumber() <= 10 || (19 <= customUserGame.getRouletteNumber() && customUserGame.getRouletteNumber() <= 28)))
                || (customUserGame.getRouletteNumber() % 2 == customUserGame.getGuessedColour() &&
                (11 <= customUserGame.getRouletteNumber() && customUserGame.getRouletteNumber() <= 18 || (29 <= customUserGame.getRouletteNumber())))
        ) {
            customUserGame.setResultMessage(congratulate);
        } else {
            customUserGame.setResultMessage(loss);
        }
        return customUserGame;
    }

    public CustomUserGame guessingThirdPart(CustomUserGame customUserGame, Integer guessedThirdPart) {
        customUserGame.setGuessedThirdPart(guessedThirdPart);
        if ((customUserGame.getGuessedThirdPart() == 1 && customUserGame.getRouletteNumber() <= 9)
                || (customUserGame.getGuessedThirdPart() == 2 && 10 <= customUserGame.getRouletteNumber() && customUserGame.getRouletteNumber() <= 18)
                || (customUserGame.getGuessedThirdPart() == 3 && 19 <= customUserGame.getRouletteNumber())) {
            customUserGame.setResultMessage(congratulate);
        } else {
            customUserGame.setResultMessage(loss);
        }
        return customUserGame;
    }


    public CustomUserGame guessingDividedByThree(CustomUserGame customUserGame, Integer guessedDividedByThree) {
        customUserGame.setGuessedDividedByThree(guessedDividedByThree);
        if ((customUserGame.getGuessedDividedByThree() == 0 && customUserGame.getRouletteNumber() % 3 == 0)
                || (customUserGame.getGuessedDividedByThree() == 1 && customUserGame.getRouletteNumber() % 3 == 1)
                || (customUserGame.getGuessedDividedByThree() == 2 && customUserGame.getRouletteNumber() % 3 == 2)) {
            customUserGame.setResultMessage(congratulate);
        } else {
            customUserGame.setResultMessage(loss);
        }
        return customUserGame;
    }

    public boolean verifyConditionOfTriesForGaming(String username, LocalDateTime currentTime) {
        int count = 0;
        for (CustomUserGame customUserGame : customUserService.findCustomUserByUsername(username).getCustomUserGames()) {
            if (customUserGame.getDateOfPlay().toLocalDate().isEqual(currentTime.toLocalDate())) {
                count++;
            }
        }
        return count == 0;
    }

    public boolean verifyConditionOfInputsForGaming(CustomUserGameForm customUserGameForm) {
        int count = 0;
        if (customUserGameForm.getGuessedNumber() != null) {
            count++;
        }
        if (customUserGameForm.getGuessedParity() != null) {
            count++;
        }
        if (customUserGameForm.getGuessedColour() != null) {
            count++;
        }
        if (customUserGameForm.getGuessedHalf() != null) {
            count++;
        }
        if (customUserGameForm.getGuessedThirdPart() != null) {
            count++;
        }
        if (customUserGameForm.getGuessedDividedByThree() != null) {
            count++;
        }
        return count == 1;
    }


}
