package com.realestate.service;

import com.realestate.domain.Address;
import com.realestate.domain.Property;
import com.realestate.dto.AddressForm;
import com.realestate.dto.AddressInfo;
import com.realestate.repository.AddressRepository;
import com.realestate.exception.AddressNotFoundException;
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
    private WeatherService weatherService;

    private PropertyService propertyService;

    @Autowired
    public AddressService(AddressRepository addressRepository, ModelMapper modelMapper, WeatherService weatherService, PropertyService propertyService) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.weatherService = weatherService;
        this.propertyService = propertyService;
    }

    public void saveAddress(AddressForm form) {
        Address toSave = modelMapper.map(form, Address.class);
        Property property = propertyService.findPropertyById(form.getPropertyId());
        toSave.setProperty(property);
        addressRepository.save(toSave);
    }

    public AddressInfo findById(Long id) {
        Address address = findAddressById(id);
        String propertyName = address.getProperty().getName();
        AddressInfo addressInfo = modelMapper.map(address, AddressInfo.class);
        addressInfo.setPropertyName(propertyName);
        return addressInfo;
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

