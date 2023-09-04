package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyService propertyService;


    @Test
    void testList_asStart_emptyList() {
        when(propertyRepository.findAll()).thenReturn(List.of());
        assertThat(propertyService.getProperties()).isEmpty();
    }



}

    PropertyServiceTest() {
    }

    @BeforeAll

    @Test
    void findAll() {
        propertyService.getProperties();
    }
}