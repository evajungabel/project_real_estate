package hu.progmasters.moovsmart.domain;

import hu.progmasters.moovsmart.domain.CustomUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "confirmationToken")
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tokenId")
    private Long tokenId;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @Column(name = "expiredDate")
    private LocalDateTime expiredDate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "custom_user_username")
    private CustomUser customUser;


}
