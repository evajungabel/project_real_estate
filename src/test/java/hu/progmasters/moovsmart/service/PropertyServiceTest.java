package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.*;
import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.AuthenticationExceptionImpl;
import hu.progmasters.moovsmart.exception.PropertyNotFoundException;
import hu.progmasters.moovsmart.repository.PropertyRepository;
import org.apache.tomcat.websocket.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CustomUserService customUserService;


    @Mock
    private PropertyImageURLService propertyImageURLService;

    @InjectMocks
    private PropertyService propertyService;
    private Property property1;
    private Property property2;
    private PropertyForm propertyForm1;
    private PropertyForm propertyForm2;

    private Property property1Update;
    private PropertyForm propertyFormUpdate;
    private PropertyInfo propertyInfo1;
    private PropertyInfo propertyInfo2;

    private PropertyInfo propertyInfo1Update;

    private PropertyDetails propertyDetails1;
    private PropertyDetails propertyDetails2;
    private CustomUser customUser1;

    private PropertyImageURL propertyImageURL;
    private PropertyImageURLForm propertyImageURLForm;
    private PropertyImageURLInfo propertyImageURLInfo;


    @BeforeEach
    void init() {

        customUser1 = new CustomUser().builder()
                .customUserId(1L)
                .username("pistike")
                .name("Kis Pistike")
                .email("pistike@gmail.com")
                .password("Pistike1*")
                .roles(List.of(CustomUserRole.ROLE_USER))
                .enable(true)
                .activation("123456789")
                .build();

        propertyImageURL = new PropertyImageURL().builder()
                .propertyImageUrlId(1L)
                .propertyImageURL("image/jpeg;base64,/2555879j/4AAQSk")
                .property(property1)
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
                .customUser(customUser1)
                .propertyImageURLs(List.of(propertyImageURL))
                .build();

        propertyImageURLInfo = new PropertyImageURLInfo().builder()
                .propertyImageURL("image/jpeg;base64,/2555879j/4AAQSk")
                .build();

        propertyImageURLForm = new PropertyImageURLForm().builder()
                .propertyImageURL("image/jpeg;base64,/2555879j/4AAQSk")
                .build();


        property2 = new Property().builder()
                .id(2L)
                .dateOfCreation(LocalDateTime.of(2021, Month.JANUARY, 1, 10, 10, 30))
                .name("Kulipintyó")
                .type(PropertyType.HOUSE)
                .price(35000000d)
                .area(85)
                .numberOfRooms(4)
                .description("Jó kis családi ház")
                .customUser(customUser1)
                .propertyImageURLs(List.of(propertyImageURL))
                .build();

        propertyForm1 = new PropertyForm().builder()
                .name("Kuckó")
                .type(PropertyType.HOUSE)
                .price(400000000d)
                .area(90d)
                .numberOfRooms(5)
                .description("Jó kis házikó")
                .build();

        property1Update = new Property().builder()
                .id(3L)
                .dateOfCreation(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 10, 30))
                .name("Buckó")
                .type(PropertyType.HOUSE)
                .price(400000000d)
                .area(91)
                .numberOfRooms(5)
                .status(PropertyStatus.ACTIVE)
                .description("Jó kis házikó")
                .customUser(customUser1)
                .build();

        propertyFormUpdate = new PropertyForm().builder()
                .name("Buckó")
                .type(PropertyType.HOUSE)
                .price(400000000d)
                .area(91d)
                .numberOfRooms(5)
                .description("Jó kis házikó")
                .build();

        propertyForm2 = new PropertyForm().builder()
                .name("Kulipintyó")
                .type(PropertyType.HOUSE)
                .price(35000000d)
                .area(85d)
                .numberOfRooms(4)
                .description("Jó kis családi ház")
                .build();



        propertyInfo1 = new PropertyInfo().builder()
                .id(1L)
                .name("Kuckó")
                .price(400000000d)
                .propertyImageURLS(List.of(propertyImageURL))
                .numberOfRooms(5)
                .build();

        propertyInfo1Update = new PropertyInfo().builder()
                .id(3L)
                .name("Buckó")
                .price(400000000d)
                .numberOfRooms(5)
                .build();

        propertyInfo2 = new PropertyInfo().builder()
                .id(2L)
                .name("Kulipintyó")
                .price(35000000d)
                .numberOfRooms(4)
                .propertyImageURLS(List.of(propertyImageURL))
                .build();

        propertyDetails1 = new PropertyDetails().builder()
                .name("Kuckó")
                .price(400000000d)
                .numberOfRooms(5)
                .description("Jó kis házikó")
                .propertyImageURLInfos(List.of(propertyImageURLInfo))
                .build();

        propertyDetails2 = new PropertyDetails().builder()
                .name("Kulipintyó")
                .price(35000000d)
                .numberOfRooms(4)
                .description("Jó kis családi ház")
                .propertyImageURLInfos(List.of(propertyImageURLInfo))
                .build();




    }

    @Test
    void test_asStart_emptyList() {
        when(propertyRepository.findAll()).thenReturn(List.of());
        assertThat(propertyService.getProperties()).isEmpty();

        verify(propertyRepository, times(1)).findAll();
        verifyNoMoreInteractions(propertyRepository);
    }

    @Test
    void test_allPropertiesWithOneProperty() {
        when(modelMapper.map(property1, PropertyDetails.class)).thenReturn(propertyDetails1);
        when(propertyRepository.findAll()).thenReturn(List.of(property1));

        assertThat(propertyService.getProperties())
                .hasSize(1)
                .containsExactly(propertyDetails1);

        verify(propertyRepository, times(1)).findAll();
        verifyNoMoreInteractions(propertyRepository);
    }

    @Test
    void test_allPropertiesWithTwoProperty() {
        when(propertyRepository.findAll()).thenReturn(List.of(property1, property2));
        when(modelMapper.map(property1, PropertyDetails.class)).thenReturn(propertyDetails1);
        when(modelMapper.map(property2, PropertyDetails.class)).thenReturn(propertyDetails2);
        assertEquals(List.of(propertyDetails1, propertyDetails2), propertyService.getProperties());

        verify(propertyRepository, times(1)).findAll();
        verifyNoMoreInteractions(propertyRepository);
    }



    @Test
    void test_getPropertyDetails_withNoId() {
        when(propertyRepository.findById(3L)).thenReturn(Optional.empty());

        try {
            propertyService.getPropertyDetails(3L);
            fail("Expected PropertyNotFoundException, but no exception was thrown.");
        } catch (PropertyNotFoundException e) {
            assertEquals("No property found with id: 3", e.getMessage());
        }
    }

    @Test
    void test_getPropertyDetails_with1LId() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.ofNullable(property1));
        when(modelMapper.map(property1, PropertyDetails.class)).thenReturn(propertyDetails1);
        assertEquals(propertyDetails1, propertyService.getPropertyDetails(1L));

        verify(propertyRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(propertyRepository);
    }

    @Test
    void test_getPropertyDetails_with2LId() {
        when(propertyRepository.findById(2L)).thenReturn(Optional.ofNullable(property2));
        when(modelMapper.map(property2, PropertyDetails.class)).thenReturn(propertyDetails2);
        assertEquals(propertyDetails2, propertyService.getPropertyDetails(2L));

        verify(propertyRepository, times(1)).findById(2L);
        verifyNoMoreInteractions(propertyRepository);
    }



    @Test
    void test_saveProperty() {
        when(modelMapper.map(propertyForm1, Property.class)).thenReturn(property1);
        when(modelMapper.map(property1, PropertyInfo.class)).thenReturn(propertyInfo1);
        when(propertyRepository.save(property1)).thenReturn(property1);

        when(customUserService.findCustomUserByUsername("pistike")).thenReturn(customUser1);

        assertEquals(propertyInfo1, propertyService.createProperty("pistike", propertyForm1));
        assertEquals(customUser1.getUsername(), property1.getCustomUser().getUsername());

        verify(propertyRepository, times(1)).save(any());
        verifyNoMoreInteractions(propertyRepository);
    }


    //TODO when username is not found


    @Test
    void test_makeInactiveProperty() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.ofNullable(property1));

        propertyService.makeInactive(1L);

        assertEquals(PropertyStatus.INACTIVE, property1.getStatus());

        verify(propertyRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(propertyRepository);
    }

    @Test
    void test_updateProperty() throws AuthenticationException {
        when(modelMapper.map(propertyFormUpdate, Property.class)).thenReturn(property1Update);
        when(modelMapper.map(property1Update, PropertyInfo.class)).thenReturn(propertyInfo1Update);
        when(propertyRepository.findById(3L)).thenReturn(Optional.ofNullable(property1Update));

        assertEquals(propertyInfo1Update, propertyService.update("pistike", 3L, propertyFormUpdate));

        verify(propertyRepository, times(1)).findById(3L);
        verifyNoMoreInteractions(propertyRepository);
    }

    @Test
    void test_createListOfImageURLs() throws AuthenticationExceptionImpl {
        when(modelMapper.map(propertyImageURLForm, PropertyImageURL.class)).thenReturn(propertyImageURL);
        when(propertyImageURLService.save(propertyImageURL)).thenReturn(propertyImageURL);
        when(propertyRepository.findById(1L)).thenReturn(Optional.ofNullable(property1));
        when(modelMapper.map(propertyImageURL, PropertyImageURLInfo.class)).thenReturn(propertyImageURLInfo);

        assertEquals(List.of(propertyImageURLInfo), propertyService.createListOfImageURLs("pistike", 1L, List.of(propertyImageURLForm)));

        verify(propertyRepository, times(2)).findById(1L);
        verify(propertyImageURLService, times(1)).save(propertyImageURL);
        verifyNoMoreInteractions(propertyRepository);
        verifyNoMoreInteractions(propertyImageURLService);
    }


}
