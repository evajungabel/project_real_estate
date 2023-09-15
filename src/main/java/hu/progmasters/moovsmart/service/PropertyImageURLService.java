package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.PropertyImageURL;
import hu.progmasters.moovsmart.repository.PropertyImageURLRepository;
import hu.progmasters.moovsmart.repository.PropertyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
