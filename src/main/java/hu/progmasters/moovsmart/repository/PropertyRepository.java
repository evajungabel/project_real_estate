package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.Address;
import hu.progmasters.moovsmart.domain.Property;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property  p " +
            " WHERE CAST(p.area AS string ) LIKE %:value%" +
            " OR CAST(p.numberOfRooms AS string ) LIKE %:value% " +
            " OR CAST(p.price AS string ) LIKE %:value% " +
            " OR CAST(p.purpose AS string ) LIKE %:value%" +
            " OR CAST(p.status AS string ) LIKE %:value%" +
            " OR CAST(p.type AS string ) LIKE %:value%")
    List<Address> findPropertyByValue(@Param("value") String value);

}
