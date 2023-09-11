package hu.progmasters.moovsmart.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

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

    @ElementCollection
    @CollectionTable(name = "agent_ratings", joinColumns = @JoinColumn(name = "estate_agent_id"))
    @MapKeyJoinColumn(name = "custom_user_id")
    @Column(name = "comment")
    private Map<Long, String> ratings;

    @OneToOne
    @JoinColumn(name = "custom_user_id")
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private CustomUser customUser;
}
