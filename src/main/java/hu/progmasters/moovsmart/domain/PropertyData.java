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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "property_condition")
    private PropertyCondition propertyCondition;

    @Column(name = "year_built")
    private Integer yearBuilt;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "parking")
    private PropertyParking propertyParking;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "orientation")
    private PropertyOrientation propertyOrientation;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "heating")
    private PropertyHeatingType propertyHeatingType;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "energy_certificate")
    private PropertyEnergyPerformanceCertificate energyCertificate;

    @Column(name = "has_balcony")
    private Boolean hasBalcony;

    @Column(name = "has_lift")
    private Boolean hasLift;

    @Column(name = "is_accessible")
    private Boolean isAccessible;

    @Column(name = "is_insulated")
    private Boolean isInsulated;

    @Column(name = "has_air_condition")
    private Boolean hasAirCondition;

    @Column(name = "has_garden")
    private Boolean hasGarden;

    @OneToOne
    @JoinColumn(name = "property_id")
    private Property property;

}
