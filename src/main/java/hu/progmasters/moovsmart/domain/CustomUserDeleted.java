package hu.progmasters.moovsmart.domain;

import hu.progmasters.moovsmart.config.CustomUserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "custom_user_deleted")
public class CustomUserDeleted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_user_deleted_id")
    private Long customUserDeletedId;

    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "e_mail")
    private String email;

    @Column(name = "is_deleted")
    private boolean isDeleted;





}
