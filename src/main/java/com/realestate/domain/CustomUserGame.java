package com.realestate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private LocalDateTime dateOfPlay;

    @Column(name = "roulette_number")
    private Integer rouletteNumber;

    @Column(name = "guessed_number")
    private Integer guessedNumber;

    @Column(name = "guessed_parity")
    private Integer guessedParity;

    @Column(name = "guessed_half")
    private Integer guessedHalf;
    @Column(name = "guessed_colour")
    private Integer guessedColour;

    @Column(name = "guessed_third_part")
    private Integer guessedThirdPart;
    @Column(name = "guessed_divided_by_three")
    private Integer guessedDividedByThree;
    @Column(name = "result_message")
    private String resultMessage;

    @ManyToOne
    @JoinColumn(name = "custom_user_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private CustomUser customUser;



}
