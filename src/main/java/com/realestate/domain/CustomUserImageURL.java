package com.realestate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "custom_user_image_url")
public class CustomUserImageURL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_user_image_url_id")
    private Long customUserImageUrlId;

    @Column(name = "custom_user_image_url")
    private String customUserImageURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_user_id")
    private CustomUser customUser;

}
