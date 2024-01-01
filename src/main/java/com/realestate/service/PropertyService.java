package com.realestate.service;

import com.realestate.domain.*;
import com.realestate.dto.*;
import com.realestate.dto.weather.Coordinate;
import com.realestate.dto.weather.WeatherData;
import com.realestate.domain.*;
import com.realestate.dto.*;
import com.realestate.exception.AuthenticationExceptionImpl;
import com.realestate.exception.NoResourceFoundException;
import com.realestate.exception.PropertyNotFoundException;
import com.realestate.exception.WeatherNotFoundException;
import com.realestate.repository.AddressRepository;
import com.realestate.repository.PropertyRepository;
import com.realestate.specifications.PropertySpecifications;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.tomcat.websocket.AuthenticationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
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
    private ExchangeService exchangeService;
    private WeatherService weatherService;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, ModelMapper modelMapper, CustomUserService customUserService, PropertyImageURLService propertyImageURLService, AddressRepository addressRepository, ExchangeService exchangeService, WeatherService weatherService) {
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper;
        this.customUserService = customUserService;
        this.propertyImageURLService = propertyImageURLService;
        this.addressRepository = addressRepository;
        this.exchangeService = exchangeService;
        this.weatherService = weatherService;
    }


    public List<PropertyDetails> getProperties() {
        List<Property> properties = propertyRepository.findAll();
        List<PropertyDetails> propertyDetailsList = new ArrayList<>();
        for (Property property : properties) {
            propertyDetailsList.add(helpFunctionMapping(property));
        }
        return propertyDetailsList;
    }

    public PropertyDetails helpFunctionMapping(Property property) {
        PropertyDetails propertyDetails = modelMapper.map(property, PropertyDetails.class);
        if (property.getAddress() != null) {
            AddressInfoForProperty addressInfoForProperties = modelMapper.map(property.getAddress(), AddressInfoForProperty.class);
            propertyDetails.setAddressInfoForProperty(addressInfoForProperties);
        }
        List<PropertyImageURLInfo> propertyImageURLInfos = property.getPropertyImageURLs().stream()
                .map(propertyImageURL -> modelMapper.map(propertyImageURL, PropertyImageURLInfo.class))
                .collect(Collectors.toList());
        propertyDetails.setPropertyImageURLInfos(propertyImageURLInfos);
        if (property.getPropertyData() != null) {
            PropertyDataInfo propertyDataInfo = modelMapper.map(property.getPropertyData(), PropertyDataInfo.class);
            propertyDetails.setPropertyDataInfo(propertyDataInfo);
        }
        return propertyDetails;
    }

    public List<PropertyDetails> getProperties24() {
        Date thresholdDate = new Date(System.currentTimeMillis() - 86400000);
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
                .map(this::helpFunctionMapping)
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
        return helpFunctionMapping(property);
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
            for (PropertyImageURL propertyImageURL : propertyImageURLs) {
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

    public void createPdf(Long id) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PropertyDetails propertyDetails = getPropertyDetails(id);

        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            Path desktopPath = FileSystems.getDefault().getPath(System.getProperty("user.home"), "Desktop");

            String filePath = desktopPath + "/property" + id + ".pdf";

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.setLeading(24.0f);

            contentStream.newLineAtOffset(100, 700);

            contentStream.showText("                  I N G A T L A N   A D A T L A P   Id.: " + id);
            contentStream.newLine();
            contentStream.newLine();
            contentStream.newLine();

            contentStream.showText("NÉV:  " + propertyDetails.getName());
            contentStream.newLine();
            contentStream.showText("VÁROS:  " + propertyDetails.getAddressInfoForProperty().getCity());
            contentStream.newLine();
            contentStream.showText("TÍPUS:  " + propertyDetails.getType().getValue());
            contentStream.newLine();
            contentStream.showText("ÁLLAPOT:  " + (propertyDetails.getPropertyDataInfo().getPropertyCondition() == null ? "N/A" : propertyDetails.getPropertyDataInfo().getPropertyCondition().getValue()));
            contentStream.newLine();
            contentStream.showText("ÁR:  " + String.format("%.2f", propertyDetails.getPrice())+ " forint");
            contentStream.newLine();
            contentStream.showText("KÍNÁL:  " + propertyDetails.getPurpose().getValue());
            contentStream.newLine();
            contentStream.showText("ALAPTERÜLET:  " + propertyDetails.getArea());
            contentStream.newLine();
            contentStream.showText("LEÍRÁS:  " + propertyDetails.getDescription());
            contentStream.newLine();
            contentStream.showText("ÉPÍTÉS ÉVE:  " + propertyDetails.getPropertyDataInfo().getYearBuilt());
            contentStream.newLine();
            contentStream.showText("FEKVÉS:  " + (propertyDetails.getPropertyDataInfo().getPropertyOrientation() == null ? "N/A" : propertyDetails.getPropertyDataInfo().getPropertyOrientation().getValue()));
            contentStream.newLine();
            contentStream.showText("FüTÉS TÍPUS:  " + (propertyDetails.getPropertyDataInfo().getPropertyHeatingType() == null ? "N/A" : propertyDetails.getPropertyDataInfo().getPropertyHeatingType().getValue()));
            contentStream.newLine();
            contentStream.showText("ENERGIA OSZTÁLY:  " + (propertyDetails.getPropertyDataInfo().getEnergyCertificate() == null ? "N/A" : propertyDetails.getPropertyDataInfo().getEnergyCertificate().getValue()));
            contentStream.newLine();
            contentStream.showText("ERKÉLY:  " + (Boolean.TRUE.equals(propertyDetails.getPropertyDataInfo().getHasBalcony()) ? "van" : Boolean.FALSE.equals(propertyDetails.getPropertyDataInfo().getHasBalcony()) ? "nincs" : "N/A"));
            contentStream.newLine();
            contentStream.showText("LIFT:  " + (Boolean.TRUE.equals(propertyDetails.getPropertyDataInfo().getHasLift()) ? "van" : Boolean.FALSE.equals(propertyDetails.getPropertyDataInfo().getHasLift()) ? "nincs" : "N/A"));
            contentStream.newLine();
            contentStream.showText("AKADÁLYMENTES:  " + (Boolean.TRUE.equals(propertyDetails.getPropertyDataInfo().getIsAccessible()) ? "igen" : Boolean.FALSE.equals(propertyDetails.getPropertyDataInfo().getIsAccessible()) ? "nem" : "N/A"));
            contentStream.newLine();
            contentStream.showText("KÜLÖNÁLLÓ:  " + (Boolean.TRUE.equals(propertyDetails.getPropertyDataInfo().getIsInsulated()) ? "van" : Boolean.FALSE.equals(propertyDetails.getPropertyDataInfo().getIsInsulated()) ? "nincs" : "N/A"));
            contentStream.newLine();
            contentStream.showText("LÉGKONDICIONÁLÓ:  " + (Boolean.TRUE.equals(propertyDetails.getPropertyDataInfo().getHasAirCondition()) ? "van" : Boolean.FALSE.equals(propertyDetails.getPropertyDataInfo().getHasAirCondition()) ? "nincs" : "N/A"));
            contentStream.newLine();
            contentStream.showText("KERT:  " + (Boolean.TRUE.equals(propertyDetails.getPropertyDataInfo().getHasGarden()) ? "van" : Boolean.FALSE.equals(propertyDetails.getPropertyDataInfo().getHasGarden()) ? "nincs" : "N/A"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = "Készült: " + dateFormat.format(new Date());
            contentStream.newLine();
            contentStream.newLine();

            contentStream.newLineAtOffset(100, 50);
            contentStream.newLine();
            contentStream.newLine();
            contentStream.showText(dateStr);

            contentStream.endText();
            contentStream.close();

            document.save(filePath);
            document.close();
        } catch (IOException e) {
            log.error("Error while generating and saving PDF", e);
            throw new RuntimeException("Error while generating and saving PDF", e);
        }
    }

    public String exchange(Long id, Currencies currency) {
        Property property = findPropertyById(id);
        Double price = property.getPrice();
        String sCurrenci = currency.toString();
        return exchangeService.changePrice(price, sCurrenci);
    }

    public WeatherData findWeather(String zipcode) {
        Coordinate coordinates = weatherService.getCoordinatesForZip(zipcode);
        if (coordinates != null) {
            return weatherService.getWeatherForCoordinates(coordinates.getLat(), coordinates.getLon());
        } else {
            return null;
        }
    }

    public AddressInfoWeather findAddressWeather(Long id) {
        Property property = findPropertyById(id);
        AddressInfoWeather addressInfo = modelMapper.map(property.getAddress(), AddressInfoWeather.class);
        String zipcode = Integer.toString(addressInfo.getZipcode());
        WeatherData weatherData = findWeather(zipcode);
        if (weatherData == null) {
            throw new WeatherNotFoundException(id);
        }
        weatherData.getTemperatureInCelsius();
        addressInfo.setWeatherData(weatherData);
        return addressInfo;
    }
}
