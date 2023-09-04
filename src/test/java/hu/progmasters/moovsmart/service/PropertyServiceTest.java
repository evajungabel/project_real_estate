package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.dto.PropertyInfo;
import hu.progmasters.moovsmart.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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

    @InjectMocks
    private PropertyService propertyService;

    private Property property1;
    private Property property2;
    private Property property3;
    private Property property4;

    @BeforeEach
    void init() {
        property1 = new Property(1, 2023-08-31, 2023-09-31, null, null, "Kuckó", "House", 90, 5, 40000000, "Jó kis házikó",
                "image/jpeg;base64,/2579j/4AAQSk", true, null, null, "SzIstvan", 1);
        property2 = new Property();
        property3 = new Property();
        property4 = new Property();
    }

    @Test
    void testList_asStart_emptyList() {
        when(propertyRepository.findAll()).thenReturn(List.of());
        assertThat(propertyService.getProperties()).isEmpty();
    }

    @Test
    void testSave_singleProperty_singlePropertySaved() {
        // adjuk meg, mi történjen, ha a repository save-je van meghívva
        when(propertyRepository.save(any())).thenReturn(property1);

        // jöhet a tényleges teszt
        PropertyInfo propertySaved = propertyService.createProperty(property1);
        assertEquals(property1, propertySaved);

        // ellenőrizzük le, hogy a mockolt erőforrások pont annyiszor voltak meghívva, ahányszor akartuk
        verify(propertyRepository, times(1)).save(any());
    }

    @Test
    void testList_singlePropertySaved_singlePropertyInList() {
        // adjuk meg, mi történjen, ha a repository list-je van meghívva
        when(propertyRepository.findAll()).thenReturn(List.of(property1));

        // jöhet a tényleges teszt
        assertThat(propertyService.getProperties())
                .hasSize(1)
                .containsExactly(property1);

        // ellenőrizzük le, hogy a mockolt erőforrások pont annyiszor voltak meghívva, ahányszor akartuk
        verify(propertyRepository, times(1)).findAll();
        verifyNoMoreInteractions(propertyRepository);
    }

//    @Test
//    void testFindByActorRated_multipleMovies_moviesOfActorRated() {
//        // határozzuk meg, hogy a mock repository mit adjon vissza
//        when(movieRepository.findByActor("Rupert Grint"))
//                .thenReturn(List.of(harryPotter4, harryPotter1));
//
//        assertThat(movieService.findByActorRated("Rupert Grint"))
//                .hasSize(2)
//                .containsExactly(harryPotter1, harryPotter4);
//
//        verify(movieRepository, times(1)).findByActor("Rupert Grint");
//        verifyNoMoreInteractions(movieRepository);
//    }




}
