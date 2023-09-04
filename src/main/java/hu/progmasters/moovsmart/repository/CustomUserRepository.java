package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {

    CustomUser findByEmail(String email);

    CustomUser findByActivation(String confirmationToken);

    CustomUser findByUsername(String username);
}
