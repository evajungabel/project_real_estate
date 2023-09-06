package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.*;
import hu.progmasters.moovsmart.dto.PropertyForm;
import hu.progmasters.moovsmart.dto.PropertyInfo;
import hu.progmasters.moovsmart.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dom4j.dom.DOMNodeHelper.insertData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PropertyService propertyService;

    private Property property1;

    private Property property2;
    private PropertyForm propertyForm1;
    private PropertyInfo propertyInfo1;
    private PropertyInfo propertyInfo2;
    private CustomUser customUser1;

    private EstateAgent estateAgent1;


    @BeforeEach
    void init() {
        ModelMapper modelMapper = new ModelMapper();


        property1 = new Property().builder()
                .id(1L)
                .dateOfCreation(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 10, 30))
                .name("Kuckó")
                .type(PropertyType.HOUSE)
                .area(90)
                .numberOfRooms(5)
                .description("Jó kis házikó")
                .imageUrl("image/jpeg;base64,/2579j/4AAQSk")
                .customUser(customUser1)
                .estateAgent(estateAgent1)
                .build();

        property2 = new Property(2L, LocalDateTime.of(2021, Month.JANUARY, 1, 10, 10, 30), LocalDateTime.of(2022, Month.JANUARY, 1, 10, 10, 30), null, null, "Kuckó", PropertyType.HOUSE, 90, 6, 40000000, "Jó kis családi ház",
                "image/jpeg;base64,/op79j/4AAQSk", true, null, null, customUser1, estateAgent1);
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

        estateAgent1 = new EstateAgent(1L, AgentRank.MEDIOR, "Hvj Bátran", 12, "hivjbatran@ingatlan.com", List.of(property1));
        propertyInfo1 = new PropertyInfo(1L, "Kuckó", 5, 40000000, "image/jpeg;base64,/2579j/4AAQSk");
        propertyInfo2 = new PropertyInfo(2L, "Kuckó", 6, 40000000, "image/jpeg;base64,/op79j/4AAQSk");
        propertyForm1 = new PropertyForm("Kuckó", PropertyType.HOUSE, 90, 5, 40000000, "Jó kis házikó",
                "image/jpeg;base64,/2579j/4AAQSk", "pistike");
    }

    @Test
    void testList_asStart_emptyList() {
        when(propertyRepository.findAll()).thenReturn(List.of());
        assertThat(propertyService.getProperties()).isEmpty();
    }

    @Test
    void testList_AllProperties1() {
        when(modelMapper.map(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(propertyInfo1);
        when(propertyRepository.findAll()).thenReturn(List.of(property1));
        assertEquals( List.of(propertyInfo1), propertyService.getProperties());
    }

    @Test
    void testList_AllProperties2() {
        when(propertyRepository.findAll()).thenReturn(List.of(property1, property2));
        when(modelMapper.map(property1, PropertyInfo.class)).thenReturn(propertyInfo1);
        when(modelMapper.map(property2, PropertyInfo.class)).thenReturn(propertyInfo2);
        assertEquals(List.of(propertyInfo1, propertyInfo2), propertyService.getProperties());
    }




//    @Test
//    void testSave_singleProperty_singlePropertySaved() {
//        when(modelMapper.map(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(propertyInfo1);
//        when(modelMapper.map(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(property1);
//        when(propertyRepository.save(any())).thenReturn(property1);
//
//        assertEquals(propertyInfo1, propertyService.createProperty(propertyForm1));
//
//        verify(propertyRepository, times(1)).save(any());
//    }

//    @Test
//    void testList_singlePropertySaved_singlePropertyInList() {
//        // adjuk meg, mi történjen, ha a repository list-je van meghívva
//        when(propertyRepository.findAll()).thenReturn(List.of(property1));
//
//        // jöhet a tényleges teszt
//        assertThat(propertyService.getProperties())
//                .hasSize(1)
//                .containsExactly(property1);
//
//        // ellenőrizzük le, hogy a mockolt erőforrások pont annyiszor voltak meghívva, ahányszor akartuk
//        verify(propertyRepository, times(1)).findAll();
//        verifyNoMoreInteractions(propertyRepository);
//    }




}
