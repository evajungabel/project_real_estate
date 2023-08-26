package hu.progmasters.moovsmart.domain;

import hu.progmasters.moovsmart.config.CustomUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "custom_user")
public class CustomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_user_id")
    private Long id;

    @Column(name = "name")
    private String name;


    @Column(name = "userName")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "e-mail")
    private String eMail;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "custom_user_role")
    private List<CustomUserRole> roles;

    @OneToMany(mappedBy = "custom_user")
    private List<Property> propertyList;

}
