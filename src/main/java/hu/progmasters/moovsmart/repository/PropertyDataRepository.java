package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.PropertyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyDataRepository extends JpaRepository<PropertyData, Long> {

    Optional<PropertyData> findByPropertyId(Long propertyId);

    void deleteByPropertyId(Long id);
}
