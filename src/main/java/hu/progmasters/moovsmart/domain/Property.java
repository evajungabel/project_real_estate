package hu.progmasters.moovsmart.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "property")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long id;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @Column(name = "date_of_sale")
    private LocalDateTime dateOfSale;

    @Column(name = "date_of_inactivation")
    private LocalDateTime dateOfInactivation;

    @Column(name = "date_of_activation")
    private LocalDateTime dateOfActivation;

    @Column(name = "name")
    private String name;

    @Enumerated(value = EnumType.STRING)
    private PropertyType type;

    @Column(name = "area")
    private Integer area;

    @Column(name = "number_of_rooms")
    private Integer numberOfRooms;

    @Column(name = "price")
    private Integer price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private PropertyStatus status;

    @OneToOne(mappedBy = "property")
    private PropertyData propertyData;

    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "custom_user_id")
    private CustomUser customUser;

}
