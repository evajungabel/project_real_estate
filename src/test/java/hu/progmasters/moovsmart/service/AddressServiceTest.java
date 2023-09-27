package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.Address;
import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.dto.AddressInfo;
import hu.progmasters.moovsmart.exception.AddressNotFoundException;
import hu.progmasters.moovsmart.repository.AddressRepository;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Data
@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private WeatherService weatherService;

    private Address address1;
    private Address address2;
    private Address address3;
    private Address address4;
    private Address address5;

    private AddressInfo addressInfo;
    private AddressInfo addressInfo2;
    private AddressInfo addressInfo3;

    @BeforeEach
    void init() {
        address1 = new Address();
        address2 = new Address();
        address3 = new Address();
        address4 = new Address();
        address5 = new Address();
        addressInfo = new AddressInfo();
        addressInfo2 = new AddressInfo();
        addressInfo3 = new AddressInfo();
    }

    @Test
    void findAddressById_test() {
        Long id = 1L;
        address1.setId(id);
        Property property = new Property();
        property.setId(2L);
        property.setName("Eladó Házs");
        address1.setProperty(property);
        when(addressRepository.findById(id)).thenReturn(Optional.of(address1));
        when(modelMapper.map(address1, AddressInfo.class)).thenReturn(addressInfo);
        addressInfo.setZipcode(2200);

        AddressInfo result = addressService.findById(id);

        assertNotNull(result);
        assertEquals(addressInfo, result);
    }

    @Test
    void AddressNotFind_test() {
        Long id = 1L;
        when(addressRepository.findById(id)).thenReturn(Optional.empty());

        try {
            addressService.findById(id);
            fail("Expected AddressNotFoundException, but no exception was thrown.");
        } catch (AddressNotFoundException e) {
            assertEquals("No address found with id: " + id, e.getMessage());
        }
    }

    @Test
    void deleteAddress_test() {
        Long id = 2L;
        address2.setId(id);
        when(addressRepository.findById(id)).thenReturn(Optional.of(address2));
        addressService.delete(id);
        assertTrue(address2.getDeleted());
    }

    @Test
    void findAddressByValue_test() {
        address4.setCity("Monarchia");
        address3.setCity("Monor");
        String value = "mon";

        when(addressRepository.findAddressByValue(value)).thenReturn(List.of(address3, address4));
        when(modelMapper.map(address3, AddressInfo.class)).thenReturn(addressInfo);
        when(modelMapper.map(address4, AddressInfo.class)).thenReturn(addressInfo2);

        List<AddressInfo> result = addressService.findByValue(value);

        assertEquals(List.of(addressInfo, addressInfo2), result);
    }
}
