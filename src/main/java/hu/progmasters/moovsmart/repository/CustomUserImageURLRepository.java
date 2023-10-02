package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.CustomUserImageURL;
import hu.progmasters.moovsmart.domain.PropertyImageURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserImageURLRepository  extends JpaRepository<CustomUserImageURL, Long> {
}
