package hu.progmasters.moovsmart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomUserGameForm {

    private Integer guessedNumber;
    private Integer guessedParity;
    private Integer guessedColour;

}
