package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.ConfirmationToken;
import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.EmailAddressExistsException;
import hu.progmasters.moovsmart.exception.EmailAddressNotFoundException;
import hu.progmasters.moovsmart.exception.UsernameExistsException;
import hu.progmasters.moovsmart.exception.UsernameNotFoundExceptionImp;
import hu.progmasters.moovsmart.repository.CustomUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomUserTest {

    @Mock
    private CustomUserRepository customUserRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomUserService customUserService;
    private CustomUser customUser1;
    private CustomUserInfo customUserInfo1;

    private CustomUserForm customUserForm1;
    private CustomUserForm customUserForm2;

    private CustomUser customUser2;
    private CustomUserInfo customUserInfo2;

    private ConfirmationToken confirmationToken;

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

        customUserForm1 = new CustomUserForm().builder()
                .username("pistike")
                .name("Kis Pistike")
                .email("pistike@gmail.com")
                .password("Pistike1*")
                .build();

        customUserForm2 = new CustomUserForm().builder()
                .username("moricka")
                .name("Rosszcsont Móricka")
                .email("rosszcsont.moricka@gmail.com")
                .password("Moricka1*")
                .build();

        customUserInfo2 = new CustomUserInfo().builder()
                .username("moricka")
                .name("Rosszcsont Móricka")
                .email("rosszcsont.moricka@gmail.com")
                .password("Moricka1*")
                .build();

        confirmationToken = new ConfirmationToken().builder()
                .tokenId(1L)
                .confirmationToken("123456")
                .createdDate(LocalDateTime.now())
                .expiredDate(LocalDateTime.now().plusMinutes(2))
                .build();
    }


    @Test
    void testList_asStart_emptyList() {
        when(customUserRepository.findAll()).thenReturn(List.of());
        assertThat(customUserService.getCustomUsers().isEmpty());

        verify(customUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(customUserRepository);
    }

//    @Test
//    void testRegister_CustomUser() {
//        when(customUserRepository.save(any())).thenReturn(customUser1);
//        when(confirmationTokenService.save(any())).thenReturn(confirmationToken);
//        mock(customUserService.deleteIfItIsNotActivated(customUser1);
//        customUserService.register(customUserForm1);
//        verify(customUserRepository).save(customUser1);
//
//        verify(customUserRepository, times(1)).findAll();
//        verifyNoMoreInteractions(customUserRepository);
//    }

    @Test
    void testRegisterCustomerUser_withExistingUsername() {
        when(customUserRepository.findByUsername("pistike")).thenReturn(customUser1);

        try {
            customUserService.register(customUserForm1);
            fail("Expected UsernameExistsException, but no exception was thrown.");
        } catch (UsernameExistsException e) {
            assertEquals("Username exists with: pistike", e.getMessage());
        }
    }


    @Test
    void testRegisterCustomerUser_withExistingEmailAddress() {
        when(customUserRepository.findByEmail("pistike@gmail.com")).thenReturn(customUser1);

        try {
            customUserService.register(customUserForm1);
            fail("Expected EmailAddressExistsException, but no exception was thrown.");
        } catch (EmailAddressExistsException e) {
            assertEquals("Email address exists with: pistike@gmail.com", e.getMessage());
        }
    }

//    @Test
//    void testRegisterCustomerUser_deleteIfItIsNotActive() {
//        customUser1.setEnable(false);
//        customUserService.deleteIfItIsNotActivated(customUser1);
//        assertEquals(null, customUser1);

//    verify(customUserRepository, times(1)).findByUsername("pistike");
//    verifyNoMoreInteractions(customUserRepository);
//    }

    @Test
    void testCustomUserActivation() {

        verify(customUserRepository, times(1)).findByUsername("pistike");
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void testLoadCustomUserByUsername() {

        verify(customUserRepository, times(1)).findByUsername("pistike");
        verifyNoMoreInteractions(customUserRepository);
    }


    @Test
    void testList_getAllCustomUsersWithOneCustomUser() {
        when(modelMapper.map(customUser1, CustomUserInfo.class)).thenReturn(customUserInfo1);
        when(customUserRepository.findAll()).thenReturn(List.of(customUser1));

        assertThat(customUserService.getCustomUsers())
                .hasSize(1)
                .containsExactly(customUserInfo1);

        verify(customUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void testList_allCustomUsersWithTwoCustomUser() {
        when(customUserRepository.findAll()).thenReturn(List.of(customUser1, customUser2));
        when(modelMapper.map(customUser1, CustomUserInfo.class)).thenReturn(customUserInfo1);
        when(modelMapper.map(customUser2, CustomUserInfo.class)).thenReturn(customUserInfo2);
        assertEquals(List.of(customUserInfo1, customUserInfo2), customUserService.getCustomUsers());

        verify(customUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void testFindCustomUserByUsername_withNoUsername() {
        when(customUserRepository.findByUsername("pistike")).thenReturn(null);

        try {
            customUserService.findCustomUserByUsername("pistike");
            fail("Expected UsernameNotFoundExceptionImp, but no exception was thrown.");
        } catch (UsernameNotFoundExceptionImp e) {
            assertEquals("Username was not found with: pistike", e.getMessage());
        }
    }

    @Test
    void testFindCustomUserByUsername_withUsernamePistike() {
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


    @Test
    void testFindCustomUserByEmail_withNoEmailAddress() {
        when(customUserRepository.findByUsername("pistike@gmail.com")).thenReturn(null);

        try {
            customUserService.findCustomUserByEmail("pistike@gmail.com");
            fail("Expected EmailAddressNotFoundException, but no exception was thrown.");
        } catch (EmailAddressNotFoundException e) {
            assertEquals("Email address was not found with: pistike@gmail.com", e.getMessage());
        }
    }

    @Test
    void testFindCustomUserByUsername_with1LId() {
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

    @Test
    void testCustomUser_sale() {

    }

    @Test
    void testCustomUser_delete() {

    }

    @Test
    void testCustomUser_update() {

    }


}