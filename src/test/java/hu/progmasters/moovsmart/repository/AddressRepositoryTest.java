package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.Address;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void empty_test() {
    }

//    @Test
//    void findAddressByValue() {
//        String value = "os";
//        List<Address> result = addressRepository.findAddressByValue(value);
//       assertThat(result).hasSize(4);
//    }
}
