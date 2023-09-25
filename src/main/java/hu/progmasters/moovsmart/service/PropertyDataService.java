package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.domain.PropertyData;
import hu.progmasters.moovsmart.dto.PropertyDataForm;
import hu.progmasters.moovsmart.dto.PropertyDataInfo;
import hu.progmasters.moovsmart.exception.NoResourceFoundException;
import hu.progmasters.moovsmart.exception.PropertyDataNotFoundException;
import hu.progmasters.moovsmart.repository.PropertyDataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Property property = propertyService.findPropertyById(id);
        PropertyData propertyData = findPropertyDataByPropertyId(property.getPropertyData().getId());
        return modelMapper.map(propertyData, PropertyDataInfo.class);
    }

    private PropertyData findPropertyDataByPropertyId(Long propertyDataId) {
        Optional<PropertyData> propertyDataOptional = propertyDataRepository.findById(propertyDataId);
        if (propertyDataOptional.isEmpty()) {
            throw new PropertyDataNotFoundException(propertyDataId);
        }
        return propertyDataOptional.get();
    }

    public PropertyDataInfo createPropertyData(PropertyDataForm propertyDataForm, Long propertyId) {
        PropertyData toSave = modelMapper.map(propertyDataForm, PropertyData.class);
        Property property = propertyService.findPropertyById(propertyId);
        toSave.setProperty(property);
        PropertyData saved = propertyDataRepository.save(toSave);
        PropertyDataInfo propertyDataInfo = modelMapper.map(saved, PropertyDataInfo.class);
        return propertyDataInfo;
    }

    public PropertyDataInfo update(PropertyDataForm propertyDataForm, Long id) {
        Property property = propertyService.findPropertyById(id);
        PropertyData propertyData = property.getPropertyData();
        modelMapper.map(propertyDataForm, propertyData);
        return modelMapper.map(propertyData, PropertyDataInfo.class);
    }

    public void deleteByPropertyId(Long id) {
        Property property = propertyService.findPropertyById(id);
        propertyDataRepository.delete(property.getPropertyData());
    }

    public List<PropertyDataInfo> getPropertyDataListPaginated(int page, int size, String sortDir, String sort) {

        PageRequest pageReq
                = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sort);

        Page<PropertyData> propertyDatas = propertyDataRepository.findAll(pageReq);
        if (page > propertyDatas.getTotalPages()) {
            throw new NoResourceFoundException(propertyDatas.getTotalPages());
        }
        return propertyDatas.getContent().stream()
                .map(propertyData -> modelMapper.map(propertyData, PropertyDataInfo.class))
                .collect(Collectors.toList());
    }
}
