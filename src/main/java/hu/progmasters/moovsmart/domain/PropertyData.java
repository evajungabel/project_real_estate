package hu.progmasters.moovsmart.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "property_data")
public class PropertyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_data_id")
    private Long id;

//    @Enumerated(value = EnumType.STRING)
//    @Column(name = "condition")
//    private PropertyCondition condition;

    @Column(name = "year_built")
    private Integer yearBuilt;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "parking")
    private PropertyParking parking;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "orientation")
    private PropertyOrientation orientation;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "heating")
    private PropertyHeatingType heating;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "energy_certificate")
    private PropertyEnergyPerformanceCertificate energyCertificate;

    @Column(name = "has_balcony")
    private boolean hasBalcony;

    @Column(name = "has_lift")
    private boolean hasLift;

    @Column(name = "is_accessible")
    private boolean isAccessible;

    @Column(name = "is_insulated")
    private boolean isInsulated;

    @Column(name = "has_air_condition")
    private boolean hasAirCondition;

    @Column(name = "has_garden")
    private boolean hasGarden;

    @OneToOne
    @JoinColumn(name = "property_id")
    private Property property;

}
