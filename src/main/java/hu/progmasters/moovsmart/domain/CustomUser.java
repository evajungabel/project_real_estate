package hu.progmasters.moovsmart.domain;

import hu.progmasters.moovsmart.config.CustomUserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "custom_user")
public class CustomUser implements UserDetails {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "e_mail", unique = true)
    private String email;

    @Column(name = "enable")
    private boolean enable;

    @Column(name = "activation")
    private String activation;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "custom_user_role")
    private List<CustomUserRole> roles;

    @OneToMany(mappedBy = "customUser")
    private List<Property> propertyList;

    @OneToOne(mappedBy = "customUser", cascade = CascadeType.ALL)
    private ConfirmationToken confirmationToken;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        Set<CustomUserRole> roles1 = (Set<CustomUserRole>) getRoles();
        for (CustomUserRole role: roles1) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }


}
