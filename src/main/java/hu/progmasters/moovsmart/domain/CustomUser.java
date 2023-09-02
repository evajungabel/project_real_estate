package hu.progmasters.moovsmart.domain;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.dto.ConfirmationToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "custom_user")
public class CustomUser implements UserDetails {

    private boolean enable;

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "e_mail", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "custom_user_role")
    private List<CustomUserRole> roles;

    @OneToMany(mappedBy = "customUser")
    private List<Property> propertyList;

    @OneToOne(mappedBy = "customUser")
    private ConfirmationToken confirmationToken;

    public CustomUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public CustomUser setName(String name) {
        this.name = name;
        return this;
    }

    public CustomUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public CustomUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public CustomUser setRoles(List<CustomUserRole> role) {
        this.roles = role;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }


}
