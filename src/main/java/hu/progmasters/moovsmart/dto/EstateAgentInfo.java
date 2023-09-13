package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.AgentRank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EstateAgentInfo {
    private AgentRank rank;
    private Integer sellPoint;
    private Map<Long, String> ratings;
    private CustomUserInfo customUser;
}
