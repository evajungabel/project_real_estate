package hu.progmasters.moovsmart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserComment {
    @NotBlank
    private String agentName;
    @NotBlank
    private String userName;
    @NotBlank
    private String comment;
}
