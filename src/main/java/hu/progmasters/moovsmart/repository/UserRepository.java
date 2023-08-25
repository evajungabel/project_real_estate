package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
