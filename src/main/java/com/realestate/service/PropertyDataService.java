package com.realestate.service;

import com.realestate.domain.Property;
import com.realestate.domain.PropertyData;
import com.realestate.dto.PropertyDataForm;
import com.realestate.dto.PropertyDataInfo;
import com.realestate.exception.AuthenticationExceptionImpl;
import com.realestate.exception.NoResourceFoundException;
import com.realestate.repository.PropertyDataRepository;
import com.realestate.exception.PropertyDataNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

    public PropertyDataInfo createPropertyData(String username, PropertyDataForm propertyDataForm, Long propertyId) throws AuthenticationExceptionImpl {
        List<Long> propertyIds = new ArrayList<>();
        for (PropertyData propertyData : propertyDataRepository.findAll()) {
            propertyIds.add(propertyData.getProperty().getId());
        }
        Property property = propertyService.findPropertyById(propertyId);
        if (username.equals(property.getCustomUser().getUsername()) && !propertyIds.contains(property.getId())) {
            PropertyData propertyData = modelMapper.map(propertyDataForm, PropertyData.class);
            propertyData.setProperty(property);
            PropertyData saved = propertyDataRepository.save(propertyData);
            return modelMapper.map(saved, PropertyDataInfo.class);
        } else {
            throw new AuthenticationExceptionImpl(username);
        }
    }

    public PropertyDataInfo update(String username, PropertyDataForm propertyDataForm, Long id) throws AuthenticationExceptionImpl {
        Property property = propertyService.findPropertyById(id);
        if (username.equals(property.getCustomUser().getUsername())) {
            PropertyData propertyData = property.getPropertyData();
            modelMapper.map(propertyDataForm, propertyData);
            return modelMapper.map(propertyData, PropertyDataInfo.class);
        } else {
            throw new AuthenticationExceptionImpl(username);
        }
    }

    public void deleteByPropertyId(String username, Long id) throws AuthenticationExceptionImpl {
        Property property = propertyService.findPropertyById(id);
        if (username.equals(property.getCustomUser().getUsername())) {
            PropertyData propertyData = property.getPropertyData();
            property.setPropertyData(null);
            propertyDataRepository.delete(propertyData);
        } else {
            throw new AuthenticationExceptionImpl(username);
        }
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
