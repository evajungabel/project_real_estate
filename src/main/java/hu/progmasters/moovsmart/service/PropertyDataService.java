package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.domain.PropertyData;
import hu.progmasters.moovsmart.dto.PropertyDataForm;
import hu.progmasters.moovsmart.dto.PropertyDataInfo;
import hu.progmasters.moovsmart.exception.PropertyDataNotFoundException;
import hu.progmasters.moovsmart.repository.PropertyDataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class PropertyDataService {

    private final PropertyDataRepository propertyDataRepository;
    private final PropertyService propertyService;

    private final ModelMapper modelMapper;

    @Autowired
    public PropertyDataService(PropertyDataRepository propertyDataRepository, PropertyService propertyService, ModelMapper modelMapper) {
        this.propertyDataRepository = propertyDataRepository;
        this.propertyService = propertyService;
        this.modelMapper = modelMapper;
    }


    public PropertyDataInfo getPropertyData(Long id) {
        PropertyData propertyData = findPropertyDataByPropertyId(id);
        return modelMapper.map(propertyData, PropertyDataInfo.class);
    }

    private PropertyData findPropertyDataByPropertyId(Long propertyId) {
        Optional<PropertyData> propertyDataOptional = propertyDataRepository.findByPropertyId(propertyId);
        if (propertyDataOptional.isEmpty()) {
            throw new PropertyDataNotFoundException(propertyId);
        }
        return propertyDataOptional.get();
    }

    public PropertyDataInfo createPropertyData(PropertyDataForm propertyDataForm, Long propertyId) {
        PropertyData toSave = modelMapper.map(propertyDataForm, PropertyData.class);
        Property property = propertyService.findPropertyById(propertyId);
        toSave.setProperty(property);
        PropertyData saved = propertyDataRepository.save(toSave);
        return modelMapper.map(saved, PropertyDataInfo.class);
    }

    public PropertyDataInfo update(PropertyDataForm propertyDataForm, Long id) {
        PropertyData toUpdate = findPropertyDataByPropertyId(id);
        modelMapper.map(propertyDataForm, toUpdate);
        return modelMapper.map(toUpdate, PropertyDataInfo.class);
    }

    public void deleteByPropertyId(Long id) {
        propertyDataRepository.deleteByPropertyId(id);
    }
}
