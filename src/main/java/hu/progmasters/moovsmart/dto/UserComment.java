package hu.progmasters.moovsmart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserComment {
    private String agentName;
    private String userName;
    private String comment;
}
