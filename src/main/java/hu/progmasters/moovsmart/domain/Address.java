package hu.progmasters.moovsmart.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "zipcode")
    private Integer zipcode;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "number_of_building")
    private Integer numberOfBuilding;

    @Column(name = "number_of_door")
    private Integer numberOfDoor;

    @OneToOne
    @JoinColumn(name = "property_id")
    private Property property;
}
