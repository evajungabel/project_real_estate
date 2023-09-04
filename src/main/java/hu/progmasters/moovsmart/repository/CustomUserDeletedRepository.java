package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.CustomUserDeleted;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomUserDeletedRepository extends JpaRepository<CustomUserDeleted, Long> {
}
