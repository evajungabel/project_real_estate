package hu.progmasters.moovsmart.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "custom_user_game")
public class CustomUserGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    @Column(name = "date_of_play")
    private LocalDate dateOfPlay;

    @Column(name = "guessed_number")
    private Integer guessedNumber;

    @Column(name = "roulette_number")
    private Integer rouletteNumber;

    @Column(name = "result_message")
    private String resultMessage;

    @OneToOne
    @JoinColumn(name = "custom_user_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private CustomUser customUser;



}
