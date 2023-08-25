package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.dto.PropertyDetails;
import hu.progmasters.moovsmart.dto.PropertyForm;
import hu.progmasters.moovsmart.dto.PropertyListItem;
import hu.progmasters.moovsmart.exception.PropertyNotFoundException;
import hu.progmasters.moovsmart.repository.PropertyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PropertyService {

    private PropertyRepository propertyRepository;

    private ModelMapper modelMapper;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, ModelMapper modelMapper) {
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper;
    }

    public List<PropertyListItem> getProperties() {
        List<Property> properties = propertyRepository.findAll();
        return Collections.singletonList(modelMapper.map(properties, PropertyListItem.class));
    }

    public PropertyDetails getPropertyDetails(Long id) {
        Property property = propertyRepository.getOne(id);
        return new PropertyDetails(property);
    }

    public void createProperty(PropertyForm propertyForm) {
       Property toSave = modelMapper.map(propertyForm,Property.class);
       propertyRepository.save(toSave);
    }

    public void delete(Long id) {
        Property toUpdate = findPropertyById(id);
        propertyRepository.delete(toUpdate);
    }

    private Property findPropertyById(Long propertyId) {
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);
        if (propertyOptional.isEmpty()) {
            throw new PropertyNotFoundException(propertyId);
        }
        return propertyOptional.get();

    }
}
