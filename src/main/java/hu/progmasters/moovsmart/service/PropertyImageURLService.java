package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.domain.PropertyImageURL;
import hu.progmasters.moovsmart.exception.PropertyNotFoundException;
import hu.progmasters.moovsmart.repository.PropertyImageURLRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class PropertyImageURLService {

    private PropertyImageURLRepository propertyImageURLRepository;
    private ModelMapper modelMapper;

    @Autowired
    public PropertyImageURLService(PropertyImageURLRepository propertyImageURLRepository, ModelMapper modelMapper) {
        this.propertyImageURLRepository = propertyImageURLRepository;
        this.modelMapper = modelMapper;
    }

    public PropertyImageURL save(PropertyImageURL toSave) {
        return propertyImageURLRepository.save(toSave);
    }


    public PropertyImageURL findPropertyImageURLById(Long propertyImageURLId) {
        Optional<PropertyImageURL> propertyImageURLOptional = propertyImageURLRepository.findById(propertyImageURLId);
        if (propertyImageURLOptional.isEmpty()) {
            throw new PropertyNotFoundException(propertyImageURLId);
        }
        return propertyImageURLOptional.get();
    }

    public void deleteById(Long propertyImageURLById) {
        propertyImageURLRepository.deleteById(propertyImageURLById);
    }
}
