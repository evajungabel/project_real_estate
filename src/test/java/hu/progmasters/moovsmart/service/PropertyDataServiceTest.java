package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.*;
import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.AuthenticationExceptionImpl;
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
import java.util.List;
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
    private Property property2;
    private Property propertyUpdate1;
    private PropertyData propertyData1;
    private PropertyData propertyData2;
    private PropertyDataInfo propertyDataInfo1;
    private PropertyDataForm propertyDataForm1;
    private PropertyData propertyData1Update;
    private PropertyDataInfo propertyDataInfo1Update;
    private PropertyDataForm propertyDataForm1Update;
    private CustomUser customUser1;


    @BeforeEach
    void init() {

        propertyData1 = new PropertyData();
        propertyData2 = new PropertyData();
        propertyDataInfo1 = new PropertyDataInfo();
        propertyData1Update = new PropertyData();
        propertyDataInfo1Update = new PropertyDataInfo();
        propertyDataForm1Update = new PropertyDataForm();
        property1 = new Property();
        property2 = new Property();
        propertyUpdate1 = new Property();
        customUser1 = new CustomUser();

        propertyData1.setId(1L);
        propertyData1.setEnergyCertificate(PropertyEnergyPerformanceCertificate.AT_LEAST_AA);
        propertyData1.setHasAirCondition(true);
        propertyData1.setHasBalcony(true);
        propertyData1.setHasLift(true);
        propertyData1.setPropertyOrientation(PropertyOrientation.EAST);
        propertyData1.setPropertyParking(PropertyParking.COURTYARD);
        propertyData1.setPropertyHeatingType(PropertyHeatingType.CENTRAL_HEATING);
        propertyData1.setHasGarden(false);
        propertyData1.setIsAccessible(true);
        propertyData1.setIsInsulated(true);
        propertyData1.setYearBuilt(1952);
        propertyData1.setProperty(property1);

        propertyData2.setId(2L);
        propertyData2.setEnergyCertificate(PropertyEnergyPerformanceCertificate.AT_LEAST_AA);
        propertyData2.setHasAirCondition(false);
        propertyData2.setHasBalcony(true);
        propertyData2.setHasLift(true);
        propertyData2.setPropertyOrientation(PropertyOrientation.WEST);
        propertyData2.setPropertyParking(PropertyParking.COURTYARD);
        propertyData2.setPropertyHeatingType(PropertyHeatingType.CENTRAL_HEATING);
        propertyData2.setHasGarden(false);
        propertyData2.setIsAccessible(true);
        propertyData2.setIsInsulated(true);
        propertyData2.setYearBuilt(1952);
        propertyData2.setProperty(property2);

        propertyDataInfo1.setEnergyCertificate(PropertyEnergyPerformanceCertificate.AT_LEAST_AA);
        propertyDataInfo1.setHasAirCondition(true);
        propertyDataInfo1.setHasBalcony(true);
        propertyDataInfo1.setHasLift(true);
        propertyDataInfo1.setPropertyOrientation(PropertyOrientation.EAST);
        propertyDataInfo1.setPropertyParking(PropertyParking.COURTYARD);
        propertyDataInfo1.setPropertyHeatingType(PropertyHeatingType.CENTRAL_HEATING);
        propertyDataInfo1.setHasGarden(false);
        propertyDataInfo1.setIsAccessible(true);
        propertyDataInfo1.setIsInsulated(true);
        propertyDataInfo1.setYearBuilt(1952);

        propertyData1Update.setId(2L);
        propertyData1Update.setEnergyCertificate(PropertyEnergyPerformanceCertificate.AT_LEAST_AA);
        propertyData1Update.setHasAirCondition(true);
        propertyData1Update.setHasBalcony(true);
        propertyData1Update.setHasLift(false);
        propertyData1Update.setPropertyOrientation(PropertyOrientation.EAST);
        propertyData1Update.setPropertyParking(PropertyParking.COURTYARD);
        propertyData1Update.setPropertyHeatingType(PropertyHeatingType.CENTRAL_HEATING);
        propertyData1Update.setHasGarden(false);
        propertyData1Update.setIsAccessible(true);
        propertyData1Update.setIsInsulated(true);
        propertyData1Update.setYearBuilt(1952);
        propertyData1Update.setProperty(property1);


        propertyDataInfo1Update.setEnergyCertificate(PropertyEnergyPerformanceCertificate.AT_LEAST_AA);
        propertyDataInfo1Update.setHasAirCondition(true);
        propertyDataInfo1Update.setHasBalcony(true);
        propertyDataInfo1Update.setHasLift(false);
        propertyDataInfo1Update.setPropertyOrientation(PropertyOrientation.EAST);
        propertyDataInfo1Update.setPropertyParking(PropertyParking.COURTYARD);
        propertyDataInfo1Update.setPropertyHeatingType(PropertyHeatingType.CENTRAL_HEATING);
        propertyDataInfo1Update.setHasGarden(false);
        propertyDataInfo1Update.setIsAccessible(true);
        propertyDataInfo1Update.setIsInsulated(true);
        propertyDataInfo1Update.setYearBuilt(1952);


        propertyDataForm1Update.setEnergyCertificate(PropertyEnergyPerformanceCertificate.AT_LEAST_AA);
        propertyDataForm1Update.setHasAirCondition(true);
        propertyDataForm1Update.setHasBalcony(true);
        propertyDataForm1Update.setHasLift(false);
        propertyDataForm1Update.setPropertyOrientation(PropertyOrientation.EAST);
        propertyDataForm1Update.setPropertyParking(PropertyParking.COURTYARD);
        propertyDataForm1Update.setPropertyHeatingType(PropertyHeatingType.CENTRAL_HEATING);
        propertyDataForm1Update.setHasGarden(false);
        propertyDataForm1Update.setIsAccessible(true);
        propertyDataForm1Update.setIsInsulated(true);
        propertyDataForm1Update.setYearBuilt(1952);


        property1.setId(1L);
        property1.setDateOfCreation(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 10, 30));
        property1.setName("Kuckó");
        property1.setType(PropertyType.HOUSE);
        property1.setPrice(400000000d);
        property1.setArea(90);
        property1.setNumberOfRooms(5);
        property1.setStatus(PropertyStatus.ACTIVE);
        property1.setDescription("Jó kis házikó");
        property1.setPropertyData(propertyData1);
        property1.setCustomUser(customUser1);

        property2.setId(2L);
        property2.setDateOfCreation(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 10, 30));
        property2.setName("Buskó");
        property2.setType(PropertyType.FLAT);
        property2.setPrice(300000000d);
        property2.setArea(40);
        property2.setNumberOfRooms(3);
        property2.setStatus(PropertyStatus.ACTIVE);
        property2.setDescription("Jó kis lakás");
        property2.setPropertyData(propertyData2);
        property2.setCustomUser(customUser1);


        propertyUpdate1.setId(2L);
        propertyUpdate1.setDateOfCreation(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 10, 30));
        propertyUpdate1.setName("Kuckó");
        propertyUpdate1.setType(PropertyType.HOUSE);
        propertyUpdate1.setPrice(400000000d);
        propertyUpdate1.setArea(90);
        propertyUpdate1.setNumberOfRooms(5);
        propertyUpdate1.setStatus(PropertyStatus.ACTIVE);
        propertyUpdate1.setDescription("Jó kis házikó");
        propertyUpdate1.setPropertyData(propertyData1);
        propertyUpdate1.setCustomUser(customUser1);


        customUser1.setCustomUserId(1L);
        customUser1.setUsername("pistike");
        customUser1.setName("Kis Pistike");
        customUser1.setEmail("pistike@gmail.com");
        customUser1.setPassword("Pistike1*");
        customUser1.setPhoneNumber("+36303333333");
        customUser1.setRoles(List.of(CustomUserRole.ROLE_USER));
        customUser1.setEnable(true);
        customUser1.setActivation("123456");
        customUser1.setPropertyList(List.of(property1));
        customUser1.setAgent(false);
        customUser1.setHasNewsletter(false);

    }


    @Test
    void test_getPropertyDataWithPropertyId() {
        when(modelMapper.map(propertyData1, PropertyDataInfo.class)).thenReturn(propertyDataInfo1);
        when(propertyService.findPropertyById(any())).thenReturn(property1);
        when(propertyDataRepository.findById(1L)).thenReturn(Optional.ofNullable(propertyData1));

        assertEquals(propertyDataInfo1, propertyDataService.getPropertyData(1L));

        verify(propertyDataRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(propertyDataRepository);
    }


    @Test
    void test_getPropertyData_withNoPropertyId() {
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
    void test_savePropertyData() throws AuthenticationExceptionImpl {
        when(propertyDataRepository.findAll()).thenReturn((List.of(propertyData2)));
        when(propertyService.findPropertyById(1L)).thenReturn(property1);
        when(modelMapper.map(propertyDataForm1, PropertyData.class)).thenReturn(propertyData1Update);
        when(propertyDataRepository.save(any())).thenReturn(propertyData1);
        when(modelMapper.map(propertyData1, PropertyDataInfo.class)).thenReturn(propertyDataInfo1);

        assertEquals(propertyDataInfo1, propertyDataService.createPropertyData("pistike", propertyDataForm1, 1L));

        verify(propertyDataRepository, times(1)).findAll();
        verify(propertyDataRepository, times(1)).save(any());
        verifyNoMoreInteractions(propertyDataRepository);
    }



    @Test
    void test_updatePropertyData() throws AuthenticationExceptionImpl {
        when(propertyService.findPropertyById(1L)).thenReturn(propertyUpdate1);
        when(modelMapper.map(propertyDataForm1Update, PropertyData.class)).thenReturn(propertyData1Update);
        when(modelMapper.map(propertyData1Update, PropertyDataInfo.class)).thenReturn(propertyDataInfo1Update);

        assertEquals(propertyDataInfo1Update, propertyDataService.update("pistike", propertyDataForm1Update, 1L));

        verify(propertyService, times(1)).findPropertyById(1L);
        verifyNoMoreInteractions(propertyService);
    }

    @Test
    void test_savePropertyDataWithWrongPropertyDataId() {
        when(propertyDataRepository.findAll()).thenReturn((List.of(propertyData1)));
        when(propertyService.findPropertyById(1L)).thenReturn(property1);
        when(modelMapper.map(propertyDataForm1, PropertyData.class)).thenReturn(propertyData1Update);
        when(propertyDataRepository.save(any())).thenReturn(propertyData1);
        when(modelMapper.map(propertyData1, PropertyDataInfo.class)).thenReturn(propertyDataInfo1);

        try {
            propertyDataService.createPropertyData("pistike", propertyDataForm1, 1L);    fail("Expected PropertyNotFoundException, but no exception was thrown.");
        } catch (AuthenticationExceptionImpl e) {
            assertEquals("User was denied with username: pistike", e.getMessage());
        }

        verify(propertyDataRepository, times(1)).findAll();
        verifyNoMoreInteractions(propertyDataRepository);
    }

    @Test
    void test_savePropertyDataWithWrongCustomUser() {
        when(propertyDataRepository.findAll()).thenReturn((List.of(propertyData2)));
        when(propertyService.findPropertyById(1L)).thenReturn(property1);
        when(modelMapper.map(propertyDataForm1, PropertyData.class)).thenReturn(propertyData1Update);
        when(propertyDataRepository.save(any())).thenReturn(propertyData1);
        when(modelMapper.map(propertyData1, PropertyDataInfo.class)).thenReturn(propertyDataInfo1);

        try {
            propertyDataService.createPropertyData("móriczka", propertyDataForm1, 1L);    fail("Expected PropertyNotFoundException, but no exception was thrown.");
        } catch (AuthenticationExceptionImpl e) {
            assertEquals("User was denied with username: móriczka", e.getMessage());
        }

        verify(propertyDataRepository, times(1)).findAll();
        verifyNoMoreInteractions(propertyDataRepository);
    }




}
