package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
    @Query("SELECT a FROM Address a " +
            " WHERE a.street LIKE %:value%" +
            " OR a.city LIKE %:value% " +
            " OR a.houseNumber LIKE %:value% " +
            " OR CAST(a.zipcode AS string ) LIKE %:value%")
    List<Address> findAddressByValue(@Param("value") String value);

}

