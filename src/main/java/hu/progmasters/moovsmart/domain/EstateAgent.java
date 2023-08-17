package hu.progmasters.moovsmart.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "rank")
    private String rank;

    @Column(name = "name")
    private String name;

    @Column(name = "sell_point")
    private  Integer sell_point;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "estateAgent")
    private EstateAgent estateAgent;


}
