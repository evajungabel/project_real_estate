package com.realestate.repository;

import com.realestate.domain.CustomUserEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserEmailRepository  extends JpaRepository<CustomUserEmail, Long> {
    CustomUserEmail findCustomUserEmailByEmail(String email);
}
