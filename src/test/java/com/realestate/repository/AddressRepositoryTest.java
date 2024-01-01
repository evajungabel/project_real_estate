package com.realestate.repository;

import com.realestate.domain.Address;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void empty_test() {
    }

    @Test
    public void findAddressByValue_test(){
        List<Address> resultList = addressRepository.findAddressByValue("mon");
        String result = resultList.get(0).getCity();
        assertEquals(result,"Monor");
    }
}
