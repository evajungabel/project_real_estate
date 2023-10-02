package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.PropertyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PropertyDataRepository extends JpaRepository<PropertyData, Long> {

}
