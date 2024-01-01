package com.realestate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


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
    @Column(name = "type")
    private PropertyType type;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "purpose")
    private PropertyPurpose purpose;

    @Column(name = "area")
    private Integer area;

    @Column(name = "number_of_rooms")
    private Integer numberOfRooms;

    @Column(name = "price")
    private Double price;

    @Column(columnDefinition = "TEXT")
    private String description;


    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private PropertyStatus status;

    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private PropertyData propertyData;

    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyImageURL> propertyImageURLs;

    @ManyToOne
    @JoinColumn(name = "custom_user_id")
    private CustomUser customUser;

}
