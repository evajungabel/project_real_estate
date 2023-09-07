package hu.progmasters.moovsmart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomUserInfo {

    private String name;
    private String username;
    private String password;

    private String email;

}
