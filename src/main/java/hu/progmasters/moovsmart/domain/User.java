package hu.progmasters.moovsmart.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String name;


    @Column(name = "userName")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "e-mail")
    private String eMail;

    @OneToMany(mappedBy = "user")
    private List<Property> propertyList;

}
