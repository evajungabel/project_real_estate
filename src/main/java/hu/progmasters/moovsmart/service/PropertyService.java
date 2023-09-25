package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.domain.PropertyImageURL;
import hu.progmasters.moovsmart.domain.PropertyStatus;
import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.AuthenticationExceptionImpl;
import hu.progmasters.moovsmart.exception.NoResourceFoundException;
import hu.progmasters.moovsmart.exception.PropertyNotFoundException;
import hu.progmasters.moovsmart.repository.AddressRepository;
import hu.progmasters.moovsmart.repository.PropertyRepository;
import hu.progmasters.moovsmart.specifications.PropertySpecifications;
import org.apache.tomcat.websocket.AuthenticationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    private AddressRepository addressRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, ModelMapper modelMapper, CustomUserService customUserService, PropertyImageURLService propertyImageURLService, AddressRepository addressRepository) {
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper;
        this.customUserService = customUserService;
        this.propertyImageURLService = propertyImageURLService;
        this.addressRepository = addressRepository;
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
        Date thresholdDate = new Date(System.currentTimeMillis() - 60000_000);
        List<Property> properties = propertyRepository.findPropertiesCreatedAfterThresholdDate(thresholdDate);
        return properties.stream()
                .map(property -> modelMapper.map(property, PropertyDetails.class))
                .collect(Collectors.toList());
    }


    public List<PropertyDetails> getPropertyListPaginatedAndSorted(int page, int size, String sortDir, String sort) {

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


    public List<PropertyFilterRequestInfo> getPropertyRequests(int page, int size, String sortDir, String sort, PropertyFilterRequestForm propertyFilterRequestForm) {
        PageRequest pageReq
                = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sort);

        Specification<Property> spec = Specification.where(null);

        if (propertyFilterRequestForm.getType() != null) {
            spec = spec.and(PropertySpecifications.hasType(propertyFilterRequestForm.getType()));
        }

        if (propertyFilterRequestForm.getPurpose() != null) {
            spec = spec.and(PropertySpecifications.hasPurpose(propertyFilterRequestForm.getPurpose()));
        }

        if (propertyFilterRequestForm.getMinArea() != null) {
            spec = spec.and(PropertySpecifications.hasAreaGreaterThanOrEqualTo(propertyFilterRequestForm.getMinArea()));
        }

        if (propertyFilterRequestForm.getMaxArea() != null) {
            spec = spec.and(PropertySpecifications.hasAreaLessThanOrEqualTo(propertyFilterRequestForm.getMaxArea()));
        }

        if (propertyFilterRequestForm.getMinNumberOfRooms() != null) {
            spec = spec.and(PropertySpecifications.hasNumberOfRoomsGreaterThanOrEqualTo(propertyFilterRequestForm.getMinNumberOfRooms()));
        }

        if (propertyFilterRequestForm.getMaxNumberOfRooms() != null) {
            spec = spec.and(PropertySpecifications.hasNumberOfRoomsLessThanOrEqualTo(propertyFilterRequestForm.getMaxNumberOfRooms()));
        }

        if (propertyFilterRequestForm.getMinPrice() != null) {
            spec = spec.and(PropertySpecifications.hasGreaterThanOrEqualTo(propertyFilterRequestForm.getMinPrice()));
        }

        if (propertyFilterRequestForm.getMaxPrice() != null) {
            spec = spec.and(PropertySpecifications.hasLessThanOrEqualTo(propertyFilterRequestForm.getMaxPrice()));
        }

        if (propertyFilterRequestForm.getAddressInfoForProperty() != null && propertyFilterRequestForm.getAddressInfoForProperty().getCountry() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyCountry(propertyFilterRequestForm.getAddressInfoForProperty().getCountry()));
        }

        if (propertyFilterRequestForm.getAddressInfoForProperty() != null && propertyFilterRequestForm.getAddressInfoForProperty().getCity() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyCity(propertyFilterRequestForm.getAddressInfoForProperty().getCity()));
        }

        if (propertyFilterRequestForm.getAddressInfoForProperty() != null && propertyFilterRequestForm.getAddressInfoForProperty().getZipcode() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyZipcode(propertyFilterRequestForm.getAddressInfoForProperty().getZipcode()));
        }

        if (propertyFilterRequestForm.getPropertyDataForm() != null && propertyFilterRequestForm.getPropertyDataForm().getPropertyCondition() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyCondition(propertyFilterRequestForm.getPropertyDataForm().getPropertyCondition()));
        }

        if (propertyFilterRequestForm.getPropertyDataForm() != null && propertyFilterRequestForm.getPropertyDataForm().getYearBuilt() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyYearBuilt(propertyFilterRequestForm.getPropertyDataForm().getYearBuilt()));
        }

        if (propertyFilterRequestForm.getPropertyDataForm() != null && propertyFilterRequestForm.getPropertyDataForm().getPropertyParking() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyPropertyParking(propertyFilterRequestForm.getPropertyDataForm().getPropertyParking()));
        }

        if (propertyFilterRequestForm.getPropertyDataForm() != null && propertyFilterRequestForm.getPropertyDataForm().getPropertyOrientation() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyPropertyOrientation(propertyFilterRequestForm.getPropertyDataForm().getPropertyOrientation()));
        }

        if (propertyFilterRequestForm.getPropertyDataForm() != null && propertyFilterRequestForm.getPropertyDataForm().getPropertyHeatingType() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyPropertyHeatingType(propertyFilterRequestForm.getPropertyDataForm().getPropertyHeatingType()));
        }

        if (propertyFilterRequestForm.getPropertyDataForm() != null && propertyFilterRequestForm.getPropertyDataForm().getEnergyCertificate() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyEnergyCertificate(propertyFilterRequestForm.getPropertyDataForm().getEnergyCertificate()));
        }

        if (propertyFilterRequestForm.getPropertyDataForm() != null && propertyFilterRequestForm.getPropertyDataForm().getHasBalcony() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyHasBalcony(propertyFilterRequestForm.getPropertyDataForm().getHasBalcony()));
        }

        if (propertyFilterRequestForm.getPropertyDataForm() != null && propertyFilterRequestForm.getPropertyDataForm().getHasLift() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyHasLift(propertyFilterRequestForm.getPropertyDataForm().getHasLift()));
        }

        if (propertyFilterRequestForm.getPropertyDataForm() != null && propertyFilterRequestForm.getPropertyDataForm().getIsAccessible() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyIsAccessible(propertyFilterRequestForm.getPropertyDataForm().getIsAccessible()));
        }

        if (propertyFilterRequestForm.getPropertyDataForm() != null && propertyFilterRequestForm.getPropertyDataForm().getHasAirCondition() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyHasAirCondition(propertyFilterRequestForm.getPropertyDataForm().getHasAirCondition()));
        }

        if (propertyFilterRequestForm.getPropertyDataForm() != null && propertyFilterRequestForm.getPropertyDataForm().getHasGarden() != null) {
            spec = spec.and(PropertySpecifications.hasPropertyHasGarden(propertyFilterRequestForm.getPropertyDataForm().getHasGarden()));
        }

        Page<Property> matchingProperties = propertyRepository.findAll(spec, pageReq);
        if (page > matchingProperties.getTotalPages()) {
            throw new NoResourceFoundException(matchingProperties.getTotalPages());
        }

        return matchingProperties.getContent().stream()
                .map(property -> {
                    PropertyFilterRequestInfo propertyFilterRequestInfo = modelMapper.map(property, PropertyFilterRequestInfo.class);
                    if (property.getAddress() != null) {
                        AddressInfoForProperty addressInfoForProperty = modelMapper.map(property.getAddress(), AddressInfoForProperty.class);
                        propertyFilterRequestInfo.setAddressInfoForProperty(addressInfoForProperty);
                    }
                    if (property.getPropertyData() != null) {
                        PropertyDataInfo propertyDataInfo = modelMapper.map(property.getPropertyData(), PropertyDataInfo.class);
                        propertyFilterRequestInfo.setPropertyDataInfo(propertyDataInfo);
                    }
                    return propertyFilterRequestInfo;
                })
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


    public PropertyInfo createProperty(String username, PropertyForm propertyForm) {
        Property toSave = modelMapper.map(propertyForm, Property.class);
        CustomUser customUser = customUserService.findCustomUserByUsername(username);
        toSave.setCustomUser(customUser);
        toSave.setDateOfCreation(LocalDateTime.now());
        Property property = propertyRepository.save(toSave);
        return modelMapper.map(property, PropertyInfo.class);
    }

    public void makeInactive(Long id) {
        Property toDelete = findPropertyById(id);
        toDelete.setStatus(PropertyStatus.INACTIVE);
        toDelete.setDateOfInactivation(LocalDateTime.now());
    }


    public PropertyInfo update(String username, Long id, PropertyForm propertyForm) throws AuthenticationException {
        Property property = findPropertyById(id);
        if (username.equals(property.getCustomUser().getUsername())) {
            modelMapper.map(propertyForm, property);
            return modelMapper.map(property, PropertyInfo.class);
        } else {
            throw new AuthenticationExceptionImpl(username);
        }
    }

    public List<PropertyImageURLInfo> createListOfImageURLs(String username, Long id, List<PropertyImageURLForm> propertyImageURLForms) throws AuthenticationExceptionImpl {
        List<PropertyImageURL> propertyImageURLs = propertyImageURLForms.stream()
                .map(propertyImageURLForm -> modelMapper.map(propertyImageURLForm, PropertyImageURL.class))
                .collect(Collectors.toList());

        Property property = findPropertyById(id);
        if (username.equals(property.getCustomUser().getUsername())) {
            for (PropertyImageURL propertyImageURL: propertyImageURLs) {
                propertyImageURL.setProperty(findPropertyById(id));
                propertyImageURLService.save(propertyImageURL);
            }
        } else {
            throw new AuthenticationExceptionImpl(username);
        }


        return propertyImageURLs.stream()
                .map(propertyImageURL -> modelMapper.map(propertyImageURL, PropertyImageURLInfo.class))
                .collect(Collectors.toList());
    }

}
