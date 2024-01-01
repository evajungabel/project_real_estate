package com.realestate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

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

    @OneToMany(mappedBy = "estateAgent")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<AgentComment> comments;

    @OneToOne
    @JoinColumn(name = "custom_user_id")
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private CustomUser customUser;
}
