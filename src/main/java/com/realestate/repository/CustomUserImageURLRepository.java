package com.realestate.repository;

import com.realestate.domain.CustomUserImageURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserImageURLRepository  extends JpaRepository<CustomUserImageURL, Long> {
}
