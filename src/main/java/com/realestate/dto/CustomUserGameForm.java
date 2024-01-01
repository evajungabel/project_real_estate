package com.realestate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomUserGameForm {

    @Min(value = 0, message = "Guessed number must be between 0 and 36!")
    @Max(value = 36, message = "Guessed number must be between 0 and 36!")
    private Integer guessedNumber;

    @Min(value = 0, message = "Guessed parity must be between 0 and 1! 0 = Even, 1 = Odd")
    @Max(value = 1, message = "Guessed parity must be between 0 and 1! 0 = Even, 1 = Odd")
    private Integer guessedParity;

    @Min(value = 1, message = "Guessed half must be between 1 and 2! 1 = First half, 2 = Second half")
    @Max(value = 2, message = "Guessed half must be between 1 and 2! 1 = First half, 2 = Second half")
    private Integer guessedHalf;

    @Min(value = 0, message = "Guessed colour must be between 0 and 1! 0 = Red, 1 = Black")
    @Max(value = 1, message = "Guessed colour must be between 0 and 1! 0 = Red, 1 = Black")
    private Integer guessedColour;

    @Min(value = 1, message = "Guessed part must be between 1 and 3! 1 = First part, 2 = Second part, 3 = Third part")
    @Max(value = 3, message = "Guessed part must be between 1 and 3! 1 = First part, 2 = Second part, 3 = Third part")
    private Integer guessedThirdPart;

    @Min(value = 0, message = "Guessed part must be between 0 and 2! 1 = Rest is 1 divided by 3, 2 = Rest is 2 divided by 3, 0 = Rest is 0 divided by 3")
    @Max(value = 2, message = "Guessed part must be between 0 and 2! 1 = Rest is 1 divided by 3, 2 = Rest is 2 divided by 3, 0 = Rest is 0 divided by 3")
    private Integer guessedDividedByThree;

}
