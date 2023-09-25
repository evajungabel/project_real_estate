package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.domain.CustomUserGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserGameRepository extends JpaRepository<CustomUserGame, Long> {
}
