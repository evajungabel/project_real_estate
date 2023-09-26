package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.*;
import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.NoResourceFoundException;
import hu.progmasters.moovsmart.exception.PropertyNotFoundException;
import hu.progmasters.moovsmart.repository.AddressRepository;
import hu.progmasters.moovsmart.repository.PropertyRepository;
import hu.progmasters.moovsmart.specifications.PropertySpecifications;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class PropertyService {

    private PropertyRepository propertyRepository;
    private CustomUserService customUserService;
    private PropertyImageURLService propertyImageURLService;
    private ModelMapper modelMapper;
    private AddressRepository addressRepository;
//    private PropertyDataService propertyDataService;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, ModelMapper modelMapper, CustomUserService customUserService, PropertyImageURLService propertyImageURLService, AddressRepository addressRepository) {
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper;
        this.customUserService = customUserService;
        this.propertyImageURLService = propertyImageURLService;
        this.addressRepository = addressRepository;
//        this.propertyDataService = propertyDataService;
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
        Date thresholdDate = new Date(System.currentTimeMillis() - 35_000);
        List<Property> properties = propertyRepository.findPropertiesCreatedAfterThresholdDate(thresholdDate);
        return properties.stream()
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
        PropertyDataInfo propertyDataInfo = modelMapper.map(property.getPropertyData(), PropertyDataInfo.class);
        propertyDetails.setPropertyDataInfo(propertyDataInfo);
        return propertyDetails;
    }

    public Property findPropertyById(Long propertyId) {
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);
        if (propertyOptional.isEmpty()) {
            throw new PropertyNotFoundException(propertyId);
        }
        return propertyOptional.get();
    }


    public PropertyDetails createProperty(PropertyForm propertyForm) {
        Property toSave = modelMapper.map(propertyForm, Property.class);
        CustomUser customUser = customUserService.findCustomUserByUsername(propertyForm.getCustomUsername());
        toSave.setCustomUser(customUser);
        toSave.setDateOfCreation(LocalDateTime.now());
        Property property = propertyRepository.save(toSave);
        return modelMapper.map(property, PropertyDetails.class);
    }

    public void makeInactive(Long id) {
        Property toDelete = findPropertyById(id);
        toDelete.setStatus(PropertyStatus.INACTIVE);
        toDelete.setDateOfInactivation(LocalDateTime.now());
    }


    public PropertyDetails update(Long id, PropertyForm propertyForm) {
        Property property = findPropertyById(id);
        modelMapper.map(propertyForm, property);
        return modelMapper.map(property, PropertyDetails.class);
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

    public void createPdf(Long id) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PropertyDetails propertyDetails = getPropertyDetails(id);

        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            Path desktopPath = FileSystems.getDefault().getPath(System.getProperty("user.home"), "Desktop");

            String filePath = desktopPath + "/property"+id+".pdf";

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.setLeading(16.0f);

            contentStream.newLineAtOffset(100, 700);

            contentStream.showText("Name: " + propertyDetails.getName());
            contentStream.newLine();
            contentStream.showText("City: " + propertyDetails.getAddressInfoForProperty().getCity());
            contentStream.newLine();
            contentStream.showText("Type: " + propertyDetails.getType());
            contentStream.newLine();
            contentStream.showText("Price: " + propertyDetails.getPrice());
            contentStream.newLine();
            contentStream.showText("purpose: " + propertyDetails.getPurpose());
            contentStream.newLine();
            contentStream.showText("area: " + propertyDetails.getArea());
            contentStream.newLine();
            contentStream.showText("description: " + propertyDetails.getDescription());
            contentStream.newLine();
            contentStream.showText("yearBuilt: " + propertyDetails.getPropertyDataInfo().getYearBuilt());
            contentStream.newLine();
            contentStream.showText("Orientation: " + propertyDetails.getPropertyDataInfo().getPropertyOrientation());
            contentStream.newLine();
            contentStream.showText("HeatingType: " + propertyDetails.getPropertyDataInfo().getPropertyHeatingType());
            contentStream.newLine();
            contentStream.showText("energyCertificate: " + propertyDetails.getPropertyDataInfo().getEnergyCertificate());
            contentStream.newLine();
            contentStream.showText("hasBalcony: " + propertyDetails.getPropertyDataInfo().getHasBalcony());
            contentStream.newLine();
            contentStream.showText("hasLift: " + propertyDetails.getPropertyDataInfo().isHasLift());
            contentStream.newLine();
            contentStream.showText("isAccessible: " + propertyDetails.getPropertyDataInfo().isAccessible());
            contentStream.newLine();
            contentStream.showText("isInsulated: " + propertyDetails.getPropertyDataInfo().isInsulated());
            contentStream.newLine();
            contentStream.showText("AirCondition: " + propertyDetails.getPropertyDataInfo().isHasAirCondition());
            contentStream.newLine();
            contentStream.showText("Garden: " + propertyDetails.getPropertyDataInfo().isHasGarden());

            contentStream.endText();
            contentStream.close();

            document.save(filePath);
            document.close();
        } catch (IOException e) {
            log.error("Error while generating and saving PDF", e);
            throw new RuntimeException("Error while generating and saving PDF", e);
        }

    }
}
