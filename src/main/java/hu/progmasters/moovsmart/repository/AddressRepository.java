package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a.zipcode, a.city , a.street, a.houseNumber " +
                        "FROM Address a " +
                        "WHERE a.street LIKE :value " +
                        "OR a.city LIKE :value " +
                        "OR a.houseNumber LIKE :value")
    List<Address> findAddressByValue(String value);
}

