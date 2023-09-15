package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.domain.PropertyImageURL;
import hu.progmasters.moovsmart.domain.PropertyStatus;
import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.PropertyNotFoundException;
import hu.progmasters.moovsmart.repository.PropertyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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


    public List<PropertyInfo> getProperties() {
        List<Property> properties = propertyRepository.findAll();
        return properties.stream()
                .map(property -> modelMapper.map(property, PropertyInfo.class))
                .collect(Collectors.toList());
    }

    public List<PropertyInfo> getProperties24() {
        List<Property> properties = propertyRepository.findAll();
        return properties.stream()
                .filter(property -> property.getDateOfCreation().isAfter(LocalDateTime.now().minusMinutes(1)))
                .map(property -> modelMapper.map(property, PropertyInfo.class))
                .collect(Collectors.toList());
    }

    public List<PropertyInfo> findPaginated(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Property> pagedResult = propertyRepository.findAll(paging);
        return pagedResult.stream()
                .map(property -> modelMapper.map(property, PropertyInfo.class))
                .collect(Collectors.toList());
    }


    public PropertyDetails getPropertyDetails(Long id) {
        Property property = findPropertyById(id);
        return modelMapper.map(property, PropertyDetails.class);
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

    public PropertyImageURLInfo createListOfImageURLs(Long id, PropertyImageURLForm propertyImageURLForm) {
        PropertyImageURL propertyImageURL = modelMapper.map(propertyImageURLForm, PropertyImageURL.class);
        propertyImageURL.setProperty(findPropertyById(id));
        return modelMapper.map(propertyImageURLService.save(propertyImageURL),PropertyImageURLInfo .class);
}
}
