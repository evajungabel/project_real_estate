package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
