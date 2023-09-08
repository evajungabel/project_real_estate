package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.dto.CustomUserInfo;
import hu.progmasters.moovsmart.repository.CustomUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserTest {

    @Mock
    private CustomUserRepository customUserRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CustomUserService customUserService;
    private CustomUser customUser1;
    private CustomUserInfo customUserInfo1;


    private CustomUser customUser2;
    private CustomUserInfo customUserInfo2;

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

        customUserInfo1 = new CustomUserInfo().builder()
                .name("Kis Pistike")
                .username("pistike")
                .email("pistike@gmail.com")
                .password("Pistike1*")
                .build();

        customUser2 = new CustomUser().builder()
                .customUserId(2L)
                .username("moricka")
                .name("Rosszcsont Móricka")
                .email("rosszcsont.moricka@gmail.com")
                .password("Moricka1*")
                .roles(List.of(CustomUserRole.ROLE_USER))
                .enable(true)
                .activation("987654321")
                .build();

        customUserInfo2 = new CustomUserInfo().builder()
                .username("moricka")
                .name("Rosszcsont Móricka")
                .email("rosszcsont.moricka@gmail.com")
                .password("Moricka1*")
                .build();
    }

    @Test
    void testList_asStart_emptyList() {
        when(customUserRepository.findAll()).thenReturn(List.of());
        assertThat(customUserService.getCustomUsers().isEmpty());

        verify(customUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void testList_allPropertiesWithOneCustomUser() {
        when(modelMapper.map(customUser1, CustomUserInfo.class)).thenReturn(customUserInfo1);
        when(customUserRepository.findAll()).thenReturn(List.of(customUser1));

        assertThat(customUserService.getCustomUsers())
                .hasSize(1)
                .containsExactly(customUserInfo1);

        verify(customUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void testList_allPropertiesWithTwoCustomUser() {
        when(customUserRepository.findAll()).thenReturn(List.of(customUser1, customUser2));
        when(modelMapper.map(customUser1, CustomUserInfo.class)).thenReturn(customUserInfo1);
        when(modelMapper.map(customUser2, CustomUserInfo.class)).thenReturn(customUserInfo2);
        assertEquals(List.of(customUserInfo1, customUserInfo2), customUserService.getCustomUsers());

        verify(customUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void testFindCustomUserByUsername_with1LId() {
        when(customUserRepository.findByUsername("pistike")).thenReturn(customUser1);
        assertEquals(customUser1, customUserService.findCustomUserByUsername("pistike"));

        verify(customUserRepository, times(1)).findByUsername("pistike");
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void testFindCustomUserByUsername_with2LId() {
        when(customUserRepository.findByUsername("moricka")).thenReturn(customUser2);
        assertEquals(customUser2, customUserService.findCustomUserByUsername("moricka"));

        verify(customUserRepository, times(1)).findByUsername("moricka");
        verifyNoMoreInteractions(customUserRepository);
    }

    //TODO kitételre is

    @Test
    void testFindCustomUserByEmail_with1LId() {
        when(customUserRepository.findByEmail("pistike@gmail.com")).thenReturn(customUser1);
        assertEquals(customUser1, customUserService.findCustomUserByEmail("pistike@gmail.com"));

        verify(customUserRepository, times(1)).findByEmail("pistike@gmail.com");
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void testFindCustomUserByEmail_with2LId() {
        when(customUserRepository.findByEmail("moricka@gmail.com")).thenReturn(customUser2);
        assertEquals(customUser2, customUserService.findCustomUserByEmail("moricka@gmail.com"));

        verify(customUserRepository, times(1)).findByEmail("moricka@gmail.com");
        verifyNoMoreInteractions(customUserRepository);
    }

    //TODO kitételre is


}