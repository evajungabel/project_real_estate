package hu.progmasters.moovsmart.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private PropertyCondition condition;

    @Column(name = "parking")
    private boolean parking;

    @Enumerated(value = EnumType.STRING)
    private PropertyOrientation orientation;

    @Column(name = "heating")
    private String heating;

    @Column(name = "balcony")
    private boolean balcony;

    @Column(name = "insulation")
    private boolean insulation;

    @Column(name = "air_conditioner")
    private boolean airConditioner;

    @Column(name = "garden")
    private boolean garden;

    @OneToOne
    @JoinColumn(name = "property_id")
    private Property property;

}