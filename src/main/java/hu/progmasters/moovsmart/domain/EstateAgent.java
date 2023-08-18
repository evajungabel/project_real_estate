package hu.progmasters.moovsmart.domain;


import lombok.AllArgsConstructor;
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
    @Column(name = "property_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private AgentRank rank;

    @Column(name = "name")
    private String name;

    @Column(name = "sell_point")
    private Integer sellPoint;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "estateAgent")
    private List<Property> propertyList;


}
