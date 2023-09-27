package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.*;
import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.PropertyDataNotFoundException;
import hu.progmasters.moovsmart.exception.PropertyNotFoundException;
import hu.progmasters.moovsmart.repository.PropertyDataRepository;
import hu.progmasters.moovsmart.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PropertyDataServiceTest {


    @Mock
    private PropertyDataRepository propertyDataRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PropertyService propertyService;


    @InjectMocks
    private PropertyDataService propertyDataService;

    private Property property1;
    private Property propertyUpdate1;
    private PropertyData propertyData1;
    private PropertyDataInfo propertyDataInfo1;
    private PropertyDataForm propertyDataForm1;
    private PropertyData propertyData1Update;
    private PropertyDataInfo propertyDataInfo1Update;
    private PropertyDataForm propertyDataForm1Update;


    @BeforeEach
    void init() {

        propertyData1 = new PropertyData().builder()
                .id(1L)
                .energyCertificate(PropertyEnergyPerformanceCertificate.AT_LEAST_AA)
                .hasAirCondition(true)
                .hasBalcony(true)
                .hasLift(true)
                .propertyOrientation(PropertyOrientation.EAST)
                .propertyParking(PropertyParking.COURTYARD)
                .propertyHeatingType(PropertyHeatingType.CENTRAL_HEATING)
                .hasGarden(false)
                .isAccessible(true)
                .isInsulated(true)
                .yearBuilt(1952)
                .build();

        propertyDataInfo1 = new PropertyDataInfo().builder()
                .energyCertificate(PropertyEnergyPerformanceCertificate.AT_LEAST_AA)
                .hasAirCondition(true)
                .hasBalcony(true)
                .hasLift(true)
                .propertyOrientation(PropertyOrientation.EAST)
                .propertyParking(PropertyParking.COURTYARD)
                .propertyHeatingType(PropertyHeatingType.CENTRAL_HEATING)
                .hasGarden(false)
                .isAccessible(true)
                .isInsulated(true)
                .yearBuilt(1952)
                .build();

        propertyDataForm1 = new PropertyDataForm().builder()
                .energyCertificate(PropertyEnergyPerformanceCertificate.AT_LEAST_AA)
                .hasAirCondition(true)
                .hasBalcony(true)
                .hasLift(true)
                .propertyOrientation(PropertyOrientation.EAST)
                .propertyParking(PropertyParking.COURTYARD)
                .propertyHeatingType(PropertyHeatingType.CENTRAL_HEATING)
                .hasGarden(false)
                .isAccessible(true)
                .isInsulated(true)
                .yearBuilt(1952)
                .build();

        propertyData1Update = new PropertyData().builder()
                .id(1L)
                .energyCertificate(PropertyEnergyPerformanceCertificate.AT_LEAST_AA)
                .hasAirCondition(true)
                .hasBalcony(true)
                .hasLift(false)
                .propertyOrientation(PropertyOrientation.EAST)
                .propertyParking(PropertyParking.COURTYARD)
                .propertyHeatingType(PropertyHeatingType.CENTRAL_HEATING)
                .hasGarden(false)
                .isAccessible(true)
                .isInsulated(true)
                .yearBuilt(1952)
                .build();

        propertyDataInfo1Update = new PropertyDataInfo().builder()
                .energyCertificate(PropertyEnergyPerformanceCertificate.AT_LEAST_AA)
                .hasAirCondition(true)
                .hasBalcony(true)
                .hasLift(false)
                .propertyOrientation(PropertyOrientation.EAST)
                .propertyParking(PropertyParking.COURTYARD)
                .propertyHeatingType(PropertyHeatingType.CENTRAL_HEATING)
                .hasGarden(false)
                .isAccessible(true)
                .isInsulated(true)
                .yearBuilt(1952)
                .build();

        propertyDataForm1Update = new PropertyDataForm().builder()
                .energyCertificate(PropertyEnergyPerformanceCertificate.AT_LEAST_AA)
                .hasAirCondition(true)
                .hasBalcony(true)
                .hasLift(false)
                .propertyOrientation(PropertyOrientation.EAST)
                .propertyParking(PropertyParking.COURTYARD)
                .propertyHeatingType(PropertyHeatingType.CENTRAL_HEATING)
                .hasGarden(false)
                .isAccessible(true)
                .isInsulated(true)
                .yearBuilt(1952)
                .build();

        property1 = new Property().builder()
                .id(1L)
                .dateOfCreation(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 10, 30))
                .name("Kuckó")
                .type(PropertyType.HOUSE)
                .price(400000000d)
                .area(90)
                .numberOfRooms(5)
                .status(PropertyStatus.ACTIVE)
                .description("Jó kis házikó")
                .propertyData(propertyData1)
                .build();

        propertyUpdate1 = new Property().builder()
                .id(1L)
                .dateOfCreation(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 10, 30))
                .name("Kuckó")
                .type(PropertyType.HOUSE)
                .price(400000000d)
                .area(90)
                .numberOfRooms(5)
                .status(PropertyStatus.ACTIVE)
                .description("Jó kis házikó")
                .propertyData(propertyData1Update)
                .build();


    }


    @Test
    void testList_getPropertyData() {
        when(modelMapper.map(propertyData1, PropertyDataInfo.class)).thenReturn(propertyDataInfo1);
        when(propertyService.findPropertyById(any())).thenReturn(property1);
        when(propertyDataRepository.findById(1L)).thenReturn(Optional.ofNullable(propertyData1));

        assertEquals(propertyDataInfo1, propertyDataService.getPropertyData(1L));

        verify(propertyDataRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(propertyDataRepository);
    }


    @Test
    void test_getPropertyData_withNoId() {
        when(propertyDataRepository.findById(1L)).thenReturn(Optional.empty());
        when(propertyService.findPropertyById(any())).thenReturn(property1);

        try {
            propertyDataService.getPropertyData(1L);
            fail("Expected PropertyDataNotFoundException, but no exception was thrown.");
        } catch (PropertyDataNotFoundException e) {
            assertEquals("No property data found with property id: 1", e.getMessage());
        }
    }


    @Test
    void testSave_singlePropertySaved() {
        when(modelMapper.map(propertyDataForm1, PropertyData.class)).thenReturn(propertyData1);
        when(propertyService.findPropertyById(1L)).thenReturn(property1);
        when(propertyDataRepository.save(any())).thenReturn(propertyData1);
        when(modelMapper.map(propertyData1, PropertyDataInfo.class)).thenReturn(propertyDataInfo1);

        assertEquals(propertyDataInfo1, propertyDataService.createPropertyData(propertyDataForm1, 1L));

        verify(propertyDataRepository, times(1)).save(any());
        verifyNoMoreInteractions(propertyDataRepository);
    }



    @Test
    void testUpdate() {
        when(propertyService.findPropertyById(1L)).thenReturn(propertyUpdate1);
        when(modelMapper.map(propertyDataForm1Update, PropertyData.class)).thenReturn(propertyData1Update);
        when(modelMapper.map(propertyData1Update, PropertyDataInfo.class)).thenReturn(propertyDataInfo1Update);

        assertEquals(propertyDataInfo1Update, propertyDataService.update(propertyDataForm1Update, 1L));

        verify(propertyService, times(1)).findPropertyById(1L);
        verifyNoMoreInteractions(propertyService);
    }


}
