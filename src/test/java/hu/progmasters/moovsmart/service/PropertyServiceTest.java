package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.*;
import hu.progmasters.moovsmart.dto.PropertyDetails;
import hu.progmasters.moovsmart.dto.PropertyForm;
import hu.progmasters.moovsmart.dto.PropertyInfo;
import hu.progmasters.moovsmart.exception.PropertyNotFoundException;
import hu.progmasters.moovsmart.repository.PropertyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CustomUserService customUserService;

    @InjectMocks
    private PropertyService propertyService;
    private Property property1;
    private Property property2;

    private Property propertyEmpty;
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
    private EstateAgent estateAgent1;


    @BeforeEach
    void init() {
        propertyEmpty = new Property();
        property1 = new Property().builder()
                .id(1L)
                .dateOfCreation(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 10, 30))
                .name("Kuckó")
                .type(PropertyType.HOUSE)
                .price(400000000)
                .area(90)
                .numberOfRooms(5)
                .status(PropertyStatus.ACTIVE)
                .description("Jó kis házikó")
                .imageUrl("image/jpeg;base64,/2579j/4AAQSk")
                .estateAgent(estateAgent1)
                .build();

        property2 = new Property().builder()
                .id(2L)
                .dateOfCreation(LocalDateTime.of(2021, Month.JANUARY, 1, 10, 10, 30))
                .name("Kulipintyó")
                .type(PropertyType.HOUSE)
                .price(35000000)
                .area(85)
                .numberOfRooms(4)
                .description("Jó kis családi ház")
                .imageUrl("image/jpeg;base64,/2555879j/4AAQSk")
                .customUser(customUser1)
                .estateAgent(estateAgent1)
                .build();

        propertyForm1 = new PropertyForm().builder()
                .name("Kuckó")
                .type(PropertyType.HOUSE)
                .price(400000000)
                .area(90)
                .numberOfRooms(5)
                .description("Jó kis házikó")
                .imageUrl("image/jpeg;base64,/2579j/4AAQSk")
                .customUsername("pistike")
                .build();

        property1Update = new Property().builder()
                .id(1L)
                .dateOfCreation(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 10, 30))
                .name("Buckó")
                .type(PropertyType.HOUSE)
                .price(400000000)
                .area(91)
                .numberOfRooms(5)
                .status(PropertyStatus.ACTIVE)
                .description("Jó kis házikó")
                .imageUrl("image/jpeg;base64,/2579j/4AAQSk")
                .estateAgent(estateAgent1)
                .build();

        propertyFormUpdate = new PropertyForm().builder()
                .name("Buckó")
                .type(PropertyType.HOUSE)
                .price(400000000)
                .area(91)
                .numberOfRooms(5)
                .description("Jó kis házikó")
                .imageUrl("image/jpeg;base64,/2579j/4AAQSk")
                .customUsername("pistike")
                .build();

        propertyForm2 = new PropertyForm().builder()
                .name("Kulipintyó")
                .type(PropertyType.HOUSE)
                .price(35000000)
                .area(85)
                .numberOfRooms(4)
                .description("Jó kis családi ház")
                .imageUrl("image/jpeg;base64,/2555879j/4AAQSk")
                .customUsername("pistike")
                .build();



        propertyInfo1 = new PropertyInfo().builder()
                .id(1L)
                .name("Kuckó")
                .price(400000000)
                .numberOfRooms(5)
                .imageUrl("image/jpeg;base64,/2579j/4AAQSk")
                .build();

        propertyInfo1Update = new PropertyInfo().builder()
                .id(1L)
                .name("Buckó")
                .price(400000000)
                .numberOfRooms(5)
                .imageUrl("image/jpeg;base64,/2579j/4AAQSk")
                .build();

        propertyInfo2 = new PropertyInfo().builder()
                .id(2L)
                .name("Kulipintyó")
                .price(35000000)
                .numberOfRooms(4)
                .imageUrl("image/jpeg;base64,/2555879j/4AAQSk")
                .build();

        propertyDetails1 = new PropertyDetails().builder()
                .id(1L)
                .name("Kuckó")
                .price(400000000)
                .numberOfRooms(5)
                .description("Jó kis házikó")
                .imageUrl("image/jpeg;base64,/2579j/4AAQSk")
                .build();

        propertyDetails2 = new PropertyDetails().builder()
                .id(2L)
                .name("Kulipintyó")
                .price(35000000)
                .numberOfRooms(4)
                .description("Jó kis családi ház")
                .imageUrl("image/jpeg;base64,/2555879j/4AAQSk")
                .build();


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

        estateAgent1 = new EstateAgent().builder()
                .id(1L)
                .rank(AgentRank.PROFESSIONAL)
                .name("Ügynök Guru")
                .email("ugynokguru@gmail.com")
                .sellPoint(98)
                .propertyList(List.of(property1, property2))
                .build();

    }

    @Test
    void testList_asStart_emptyList() {
        when(propertyRepository.findAll()).thenReturn(List.of());
        assertThat(propertyService.getProperties()).isEmpty();

        verify(propertyRepository, times(1)).findAll();
        verifyNoMoreInteractions(propertyRepository);
    }

    @Test
    void testList_allPropertiesWithOneProperty() {
        when(modelMapper.map(property1, PropertyInfo.class)).thenReturn(propertyInfo1);
        when(propertyRepository.findAll()).thenReturn(List.of(property1));

        assertThat(propertyService.getProperties())
                .hasSize(1)
                .containsExactly(propertyInfo1);

        verify(propertyRepository, times(1)).findAll();
        verifyNoMoreInteractions(propertyRepository);
    }

    @Test
    void testList_allPropertiesWithTwoProperty() {
        when(propertyRepository.findAll()).thenReturn(List.of(property1, property2));
        when(modelMapper.map(property1, PropertyInfo.class)).thenReturn(propertyInfo1);
        when(modelMapper.map(property2, PropertyInfo.class)).thenReturn(propertyInfo2);
        assertEquals(List.of(propertyInfo1, propertyInfo2), propertyService.getProperties());

        verify(propertyRepository, times(1)).findAll();
        verifyNoMoreInteractions(propertyRepository);
    }

//    @Test
//    void testList_PaginatedPropertiesPageNo1PageSize1() {
//        when(propertyRepository.findAll(PageRequest.of(1, 1))).thenReturn(Page(property1));
//        when(modelMapper.map(property1, PropertyInfo.class)).thenReturn(propertyInfo1);
//        when(modelMapper.map(property2, PropertyInfo.class)).thenReturn(propertyInfo2);
//        assertEquals(List.of(propertyInfo1), propertyService.findPaginated(1, 1));
//
//    verify(propertyRepository, times(1)).findAll();
//    verifyNoMoreInteractions(propertyRepository);
//    }

//    @Test
//    void testGetPropertyDetails_withWrongId() {
//        when(propertyRepository.findById(3L)).thenReturn(PropertyNotFoundException);
//        when(modelMapper.map(property2, PropertyDetails.class)).thenReturn(propertyDetails2);
//        assertThrows(PropertyNotFoundException.class, (Executable) propertyService.getPropertyDetails(3L));
//
//    verifyNoMoreInteractions(propertyRepository);
//    verify(propertyRepository, times(1)).findById();
//    }

//    @Test
//    void testExpectedException() {
//
//        PropertyNotFoundException thrown = Assertions.assertThrows(PropertyNotFoundException.class, () -> {
//        when(modelMapper.map(property2, PropertyDetails.class)).thenReturn(propertyDetails2);
//            when(propertyRepository.findById(3L)).thenReturn(Optional.ofNullable(property1));
//            propertyService.getPropertyDetails(3L);
//        });
//
//        Assertions.assertEquals("some message", thrown.getMessage());
//
//    verifyNoMoreInteractions(propertyRepository);
//    verify(propertyRepository, times(1)).findById();
//    }
//
//    @Test
//    void verifiesTypeAndMessage() {
//        assertThatThrownBy(new PropertyService(propertyRepository, customUserService, modelMapper)::getProperties)
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Runtime exception occurred")
//                .hasMessageStartingWith("Runtime")
//                .hasMessageEndingWith("occurred")
//                .hasMessageContaining("exception")
//                .hasNoCause();
//
//    verify(propertyRepository, times(1)).findAll();
//    verifyNoMoreInteractions(propertyRepository);
//    }

    @Test
    void testGetPropertyDetails_withNoId() {
        when(propertyRepository.findById(3L)).thenReturn(Optional.empty());

        try {
            propertyService.getPropertyDetails(3L);
            fail("Expected PropertyNotFoundException, but no exception was thrown.");
        } catch (PropertyNotFoundException e) {
            assertEquals("No property found with id: 3", e.getMessage());
        }
    }

//    @Test
//    void testGetPropertyDetails_withNoId() {
//        when(propertyService.getPropertyDetails(3L)).thenThrow(PropertyNotFoundException.class);
//
//        assertThrows(PropertyNotFoundException.class, () -> "");
//    }
    @Test
    void testGetPropertyDetails_with1LId() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.ofNullable(property1));
        when(modelMapper.map(property1, PropertyDetails.class)).thenReturn(propertyDetails1);
        assertEquals(propertyDetails1, propertyService.getPropertyDetails(1L));

        verify(propertyRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(propertyRepository);
    }

    @Test
    void testGetPropertyDetails_with2LId() {
        when(propertyRepository.findById(2L)).thenReturn(Optional.ofNullable(property2));
        when(modelMapper.map(property2, PropertyDetails.class)).thenReturn(propertyDetails2);
        assertEquals(propertyDetails2, propertyService.getPropertyDetails(2L));

        verify(propertyRepository, times(1)).findById(2L);
        verifyNoMoreInteractions(propertyRepository);
    }



    @Test
    void testSave_singleProperty_singlePropertySaved() {
        when(modelMapper.map(propertyForm1, Property.class)).thenReturn(property1);
        when(modelMapper.map(property1, PropertyInfo.class)).thenReturn(propertyInfo1);
        when(propertyRepository.save(property1)).thenReturn(property1);

        when(customUserService.findCustomUserByUsername(propertyForm1.getCustomUsername())).thenReturn(customUser1);

        assertEquals(propertyInfo1, propertyService.createProperty(propertyForm1));
        assertEquals(customUser1.getUsername(), property1.getCustomUser().getUsername());

        verify(propertyRepository, times(1)).save(any());
        verifyNoMoreInteractions(propertyRepository);
    }

    //TODO hibakezelés itt is customUserNotFound


    @Test
    void testMakeInactive() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.ofNullable(property1));

        propertyService.makeInactive(1L);

        assertEquals(PropertyStatus.INACTIVE, property1.getStatus());

        verify(propertyRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(propertyRepository);
    }

//    @Test
//    void testUpdate() {
//        when(propertyRepository.findById(1L)).thenReturn(Optional.ofNullable(property1Update));
//        when(modelMapper.map(propertyFormUpdate, Property.class)).thenReturn(property1Update);
//        when(modelMapper.map(property1Update, PropertyInfo.class)).thenReturn(propertyInfo1Update);
//
//
//        assertEquals(propertyInfo1Update, propertyService.update(1L, propertyFormUpdate));
//
//
//        verify(propertyRepository, times(1)).findById(1L);
//        verifyNoMoreInteractions(propertyRepository);
//    }



}
