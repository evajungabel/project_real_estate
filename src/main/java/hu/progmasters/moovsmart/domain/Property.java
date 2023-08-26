package hu.progmasters.moovsmart.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "property")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(value = EnumType.STRING)
    private PropertyType type;

    @Column(name = "space")
    private String space;

    @Column(name = "number_of_rooms")
    private Integer numberOfRooms;

    @Column(name = "price")
    private Integer price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToOne(mappedBy = "property")
    private PropertyData propertyData;

    @OneToOne(mappedBy = "property")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "custom_user_id")
    private CustomUser customUser;

    @ManyToOne
    @JoinColumn(name = "estate_agent_id")
    private EstateAgent estateAgent;
}
