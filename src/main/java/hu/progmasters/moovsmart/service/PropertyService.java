package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.domain.PropertyImageURL;
import hu.progmasters.moovsmart.domain.PropertyStatus;
import hu.progmasters.moovsmart.domain.SimplePage;
import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.NoResourceFoundException;
import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.PropertyNotFoundException;
import hu.progmasters.moovsmart.repository.PropertyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PropertyService {

    private PropertyRepository propertyRepository;
    private CustomUserService customUserService;

    private PropertyImageURLService propertyImageURLService;
    private ModelMapper modelMapper;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, ModelMapper modelMapper, CustomUserService customUserService, PropertyImageURLService propertyImageURLService) {
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper;
        this.customUserService = customUserService;
        this.propertyImageURLService = propertyImageURLService;
    }


    public List<PropertyDetails> getProperties() {
        List<Property> properties = propertyRepository.findAll();
        List<PropertyDetails> propertyDetailsList = new ArrayList<>();
        for (Property property : properties) {
            PropertyDetails propertyDetails = modelMapper.map(property, PropertyDetails.class);
            AddressInfoForProperty addressInfoForProperties = modelMapper.map(property.getAddress(), AddressInfoForProperty.class);
            propertyDetails.setAddressInfoForProperty(addressInfoForProperties);
            propertyDetailsList.add(propertyDetails);
        }
        return propertyDetailsList;
    }

    public List<PropertyDetails> getProperties24() {
        List<Property> properties = propertyRepository.findAll();
        return properties.stream()
                .filter(property -> property.getDateOfCreation().isAfter(LocalDateTime.now().minusSeconds(10)))
                .map(property -> modelMapper.map(property, PropertyDetails.class))
                .collect(Collectors.toList());
    }


    public List<PropertyDetails> getPropertyListPaginated(int page, int size, String sortDir, String sort) {

        PageRequest pageReq
                = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sort);

        Page<Property> properties = propertyRepository.findAll(pageReq);
        if (page > properties.getTotalPages()) {
            throw new NoResourceFoundException(properties.getTotalPages());
        }
        return properties.getContent().stream()
                .map(property -> modelMapper.map(property, PropertyDetails.class))
                .collect(Collectors.toList());
    }




    public PropertyDetails getPropertyDetails(Long id) {
        Property property = findPropertyById(id);
        PropertyDetails propertyDetails = modelMapper.map(property, PropertyDetails.class);
        AddressInfoForProperty addressInfoForProperty = modelMapper.map(property.getAddress(), AddressInfoForProperty.class);
        propertyDetails.setAddressInfoForProperty(addressInfoForProperty);
        return propertyDetails;
    }

    public Property findPropertyById(Long propertyId) {
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);
        if (propertyOptional.isEmpty()) {
            throw new PropertyNotFoundException(propertyId);
        }
        return propertyOptional.get();
    }


    public PropertyInfo createProperty(PropertyForm propertyForm) {
        Property toSave = modelMapper.map(propertyForm, Property.class);
        CustomUser customUser = customUserService.findCustomUserByUsername(propertyForm.getCustomUsername());
        toSave.setCustomUser(customUser);
        toSave.setDateOfCreation(LocalDateTime.now());
        Property property = propertyRepository.save(toSave);
        return modelMapper.map(property, PropertyInfo.class);
    }

    public void makeInactive(Long id) {
        Property toDelete = findPropertyById(id);
        toDelete.setStatus(PropertyStatus.INACTIVE);
    }


    public PropertyInfo update(Long id, PropertyForm propertyForm) {
        Property property = findPropertyById(id);
        modelMapper.map(propertyForm, property);
        return modelMapper.map(property, PropertyInfo.class);
    }

    public List<PropertyImageURLInfo> createListOfImageURLs(Long id, List<PropertyImageURLForm> propertyImageURLForms) {
        List<PropertyImageURL> propertyImageURLs = propertyImageURLForms.stream()
                .map(propertyImageURLForm -> {
                    PropertyImageURL propertyImageURL = modelMapper.map(propertyImageURLForm, PropertyImageURL.class);
                    propertyImageURL.setProperty(findPropertyById(id));
                    propertyImageURLService.save(propertyImageURL);
                    return propertyImageURL;
                })
                .collect(Collectors.toList());

        return propertyImageURLs.stream()
                .map(propertyImageURL -> modelMapper.map(propertyImageURL, PropertyImageURLInfo.class))
                .collect(Collectors.toList());
    }

}
