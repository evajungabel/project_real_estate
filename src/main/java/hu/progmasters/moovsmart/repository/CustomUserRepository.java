package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, String> {

    CustomUser findByEmail(String email);

}
