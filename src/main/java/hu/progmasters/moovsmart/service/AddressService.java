package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.Address;
import hu.progmasters.moovsmart.dto.AddressForm;
import hu.progmasters.moovsmart.dto.AddressInfo;
import hu.progmasters.moovsmart.exception.AddressNotFoundException;
import hu.progmasters.moovsmart.repository.AddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public AddressInfo findById(Long id) {
        return modelMapper.map(findAddressById(id), AddressInfo.class);
    }

    public Address findAddressById(Long id) {
        Optional<Address> addressOptional = addressRepository.findById(id);
        if (addressOptional.isEmpty()) {
            throw new AddressNotFoundException(id);
        }
        return addressOptional.get();
    }

    public void delete(Long id) {
        Address toDelete = findAddressById(id);
        toDelete.setDeleted(true);
        toDelete.setProperty(null);
    }

    public AddressInfo update(Long id, AddressForm form) {
        Address toUpdate = findAddressById(id);
        modelMapper.map(form, toUpdate);
        return modelMapper.map(toUpdate, AddressInfo.class);
    }

    public List<AddressInfo> findByValue(String value) {
        List<Address> addresses = addressRepository.findAddressByValue(value);
        List<AddressInfo> addressInfoList = new ArrayList<>();
        for (Address address : addresses) {
            AddressInfo addressInfo = modelMapper.map(address, AddressInfo.class);
            addressInfoList.add(addressInfo);
        }
        return addressInfoList;
    }

    public List<AddressInfo> findAllAddress() {
        List<Address> addresses = addressRepository.findAll();
        List<AddressInfo> addressInfoList = new ArrayList<>();
        for (Address address : addresses) {
            AddressInfo addressInfo = modelMapper.map(address, AddressInfo.class);
            addressInfoList.add(addressInfo);
        }
        return addressInfoList;
    }
}

