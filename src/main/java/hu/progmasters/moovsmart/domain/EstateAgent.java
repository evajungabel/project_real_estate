package hu.progmasters.moovsmart.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "estate_agent")
public class EstateAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estate_agent_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "agent_rank")
    private AgentRank rank;

    @Column(name = "sell_point")
    private Integer sellPoint;

    @OneToOne
    @JoinColumn(name = "custom_user_id")
    private CustomUser customUser;
}
