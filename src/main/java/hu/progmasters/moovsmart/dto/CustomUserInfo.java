package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.config.CustomUserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomUserInfo {
    //tú sok minden jön föl. Kód legalább is biztos nem kell.
    private String name;
    private String username;
    private String email;
    private CustomUserRole roles; // ez valamiért null. Lehet azért mert csak rolessnek kéne lennie
}
