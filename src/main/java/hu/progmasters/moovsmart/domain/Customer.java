package hu.progmasters.moovsmart.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "last_name")
    private String lastName;

    @OneToOne
    @JoinColumn(name = "property_id")
    private Property property;

}
