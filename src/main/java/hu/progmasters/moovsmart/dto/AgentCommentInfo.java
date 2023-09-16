package hu.progmasters.moovsmart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentCommentInfo {
    private String customUsername;
    private String comment;
}
