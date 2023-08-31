package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.Address;
import hu.progmasters.moovsmart.dto.AddressForm;
import hu.progmasters.moovsmart.repository.AddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddressService {

    private AddressRepository addressRepository;
    private ModelMapper modelMapper;
    
    @Autowired
    public AddressService(AddressRepository addressRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    public void saveAddress(AddressForm form) {
        Address toSave = modelMapper.map(form, Address.class);
        addressRepository.save(toSave);
    }
}
