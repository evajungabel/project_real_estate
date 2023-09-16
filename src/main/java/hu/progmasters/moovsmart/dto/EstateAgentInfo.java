package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.AgentRank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EstateAgentInfo {
    private CustomUserInfo customUser;
    private AgentRank rank;
    private Integer sellPoint;
    private List<AgentCommentInfo> commentInfo;
}
