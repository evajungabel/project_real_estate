package hu.progmasters.moovsmart.domain;

import hu.progmasters.moovsmart.dto.PropertyForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


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

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "space")
    private String space;
    @Min(value = 1)
    @Max(value = 12)
    @Column(name = "number_of_rooms")
    private  Integer numberOfRooms;

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

    @OneToOne(mappedBy = "property")
    private Customer customer;


    @OneToOne
    @JoinColumn(name = "estateAgent")
    private EstateAgent estateAgent;


    public Property(PropertyForm propertyForm) {
        this.name = propertyForm.getName();
        this.numberOfRooms = propertyForm.getNumberOfRooms();
        this.price = propertyForm.getPrice();
        this.description = propertyForm.getDescription();
        this.imageUrl = propertyForm.getImageUrl();
    }
}
