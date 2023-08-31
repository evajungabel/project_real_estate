package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.dto.PropertyDetails;
import hu.progmasters.moovsmart.dto.PropertyForm;
import hu.progmasters.moovsmart.dto.PropertyInfo;
import hu.progmasters.moovsmart.exception.PropertyNotFoundException;
import hu.progmasters.moovsmart.repository.PropertyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<PropertyInfo> getProperties() {
        List<Property> properties = propertyRepository.findAll();
        List<PropertyInfo> propertyInfos = properties.stream()
                .map(property -> modelMapper.map(properties, PropertyInfo.class))
                .collect(Collectors.toList());
        return propertyInfos;
    }

    public List<PropertyInfo> findPaginated(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Property> pagedResult = propertyRepository.findAll(paging);
        List<PropertyInfo> propertyInfos = pagedResult.stream()
                .map(property -> modelMapper.map(property, PropertyInfo.class))
                .collect(Collectors.toList());
        return propertyInfos;
    }


    public PropertyDetails getPropertyDetails(Long id) {
        Property property = findPropertyById(id);
        return modelMapper.map(property, PropertyDetails.class);
    }

    public PropertyInfo createProperty(PropertyForm propertyForm) {
       Property toSave = modelMapper.map(propertyForm,Property.class);
       Property property = propertyRepository.save(toSave);
       return modelMapper.map(property, PropertyInfo.class);
    }

    public void makeInactive(Long id) {
        Property toUpdate = findPropertyById(id);
        toUpdate.setActive(false);
    }

    private Property findPropertyById(Long propertyId) {
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);
        if (propertyOptional.isEmpty()) {
            throw new PropertyNotFoundException(propertyId);
        }
        return propertyOptional.get();

    }

    public PropertyInfo update(Long id, PropertyForm propertyForm) {
        Property property = findPropertyById(id);
        modelMapper.map(propertyForm, property);
        return modelMapper.map(property, PropertyInfo.class);
    }
}
