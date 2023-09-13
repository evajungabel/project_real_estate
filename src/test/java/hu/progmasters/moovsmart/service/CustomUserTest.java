package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.*;
import hu.progmasters.moovsmart.dto.CustomUserForm;
import hu.progmasters.moovsmart.dto.CustomUserInfo;
import hu.progmasters.moovsmart.exception.EmailAddressExistsException;
import hu.progmasters.moovsmart.exception.EmailAddressNotFoundException;
import hu.progmasters.moovsmart.exception.UsernameExistsException;
import hu.progmasters.moovsmart.exception.UsernameNotFoundExceptionImp;
import hu.progmasters.moovsmart.repository.ConfirmationTokenRepository;
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
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
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


    @Mock
    private EstateAgentService estateAgentService;

    @Mock
    private CustomUserEmailService customUserEmailService;
    @InjectMocks
    private CustomUserService customUserService;
    private CustomUser customUser1;
    private CustomUserInfo customUserInfo1;

    private CustomUserForm customUserForm1;
    private CustomUserForm customUserForm2;

    private CustomUser customUser2;
    private CustomUserInfo customUserInfo2;

    private CustomUser customUserDeleted;
    private Property property1;

    private ConfirmationToken confirmationToken1;
    private ConfirmationToken confirmationToken2;

    private User customUserLoggedIn1;

    private CustomUserEmail customUserEmail;

    private List<String> listOfUsernames = new ArrayList<>();
    private List<String> listOfEmails = new ArrayList<>();
    @BeforeEach
    void init() {
        confirmationToken1 = new ConfirmationToken().builder()
                .tokenId(1L)
                .confirmationToken("123456")
                .createdDate(LocalDateTime.now())
                .expiredDate(LocalDateTime.now().plusMinutes(1))
                .customUser(customUser1)
                .build();

        property1 = new Property().builder()
                .id(1L)
                .customUser(customUser1)
                .status(PropertyStatus.INACTIVE)
                .build();

        customUser1 = new CustomUser().builder()
                .customUserId(1L)
                .username("pistike")
                .name("Kis Pistike")
                .email("pistike@gmail.com")
                .password("Pistike1*")
                .phoneNumber("+36303333333")
                .roles(List.of(CustomUserRole.ROLE_USER))
                .enable(true)
                .activation("123456")
                .confirmationToken(confirmationToken1)
                .propertyList(List.of(property1))
                .isAgent(false)
                .hasNewsletter(false)
                .build();

        customUserDeleted = new CustomUser().builder()
                .customUserId(1L)
                .username(null)
                .name(null)
                .email(null)
                .password(null)
                .phoneNumber(null)
                .roles(List.of())
                .enable(false)
                .activation(null)
                .confirmationToken(null)
                .propertyList(List.of())
                .hasNewsletter(false)
                .build();

        customUserInfo1 = new CustomUserInfo().builder()
                .name("Kis Pistike")
                .username("pistike")
                .email("pistike@gmail.com")
                .customUserRoles(List.of(CustomUserRole.ROLE_USER))
                .phoneNumber("+36303333333")
                .build();

        confirmationToken2 = new ConfirmationToken().builder()
                .tokenId(2L)
                .confirmationToken("654321")
                .createdDate(LocalDateTime.now())
                .expiredDate(LocalDateTime.now().plusNanos(1))
                .customUser(customUser2)
                .build();

        customUser2 = new CustomUser().builder()
                .customUserId(2L)
                .username("moricka")
                .name("Rosszcsont Móricka")
                .email("rosszcsont.moricka@gmail.com")
                .password("Moricka1*")
                .phoneNumber("+36303333334")
                .roles(List.of(CustomUserRole.ROLE_AGENT))
                .activation("654321")
                .confirmationToken(confirmationToken2)
                .enable(true)
                .isAgent(true)
                .hasNewsletter(true)
                .activation("987654321")
                .build();

        customUserForm1 = new CustomUserForm().builder()
                .username("pistike")
                .name("Kis Pistike")
                .email("pistike@gmail.com")
                .phoneNumber("+36303333333")
                .password("Pistike1*")
                .isAgent(false)
                .hasNewsletter(false)
                .build();

        customUserForm2 = new CustomUserForm().builder()
                .username("moricka")
                .name("Rosszcsont Móricka")
                .email("rosszcsont.moricka@gmail.com")
                .password("Moricka1*")
                .isAgent(true)
                .hasNewsletter(true)
                .build();

        customUserInfo2 = new CustomUserInfo().builder()
                .username("moricka")
                .name("Rosszcsont Móricka")
                .phoneNumber("+36303333333")
                .email("rosszcsont.moricka@gmail.com")
                .customUserRoles(List.of(CustomUserRole.ROLE_AGENT))
                .build();

        customUserEmail = new CustomUserEmail().builder()
                .customUserEmailId(1L)
                .customUser(customUser2)
                .email("rosszcsont.moricka@gmail.com")
                .build();

        customUserLoggedIn1 = (User) User
                .withUsername("pistike")
                .authorities(AuthorityUtils.createAuthorityList(String.valueOf(List.of(CustomUserRole.ROLE_USER))))
                .password("Pistike1*")
                .build();

        listOfUsernames = List.of("bob");

        listOfEmails = List.of("bob@gmail.com");
    }


    @Test
    void test_List_asStart_emptyList() {
        when(customUserRepository.findAll()).thenReturn(List.of());
        assertThat(customUserService.getCustomUsers().isEmpty());

        verify(customUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_Register_CustomUserAsUser() {
        when(confirmationTokenService.save(any())).thenReturn(confirmationToken1);
        when(passwordEncoder.encode(any())).thenReturn("Pistike1*");
        when(customUserRepository.findByEmail(customUserForm1.getEmail())).thenReturn(null);
        when(customUserRepository.findByUsername(customUserForm1.getUsername())).thenReturn(null);

        when(customUserRepository.save(any(CustomUser.class))).thenReturn(customUser1);
        when(modelMapper.map(customUser1, CustomUserInfo.class)).thenReturn(customUserInfo1);

        assertEquals(customUserInfo1, customUserService.register(customUserForm1));

        verify(customUserRepository, times(1)).findByEmail(any());
        verify(customUserRepository, times(1)).findByUsername(any());
        verify(customUserRepository, times(1)).save(any());
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_Register_CustomUserAsAgent() {
        when(confirmationTokenService.save(any())).thenReturn(confirmationToken1);
        when(passwordEncoder.encode(any())).thenReturn("Moricka1*");
        when(customUserRepository.findByEmail(customUserForm2.getEmail())).thenReturn(null);
        when(customUserRepository.findByUsername(customUserForm2.getUsername())).thenReturn(null);
        when(estateAgentService.save(customUser2)).thenReturn(customUserInfo2);

        when(customUserRepository.save(any(CustomUser.class))).thenReturn(customUser2);
        when(customUserEmailService.save(any(CustomUserEmail.class))).thenReturn(customUserEmail);
        when(modelMapper.map(customUser2, CustomUserInfo.class)).thenReturn(customUserInfo2);

        assertEquals(customUserInfo2, customUserService.register(customUserForm2));

        verify(customUserRepository, times(1)).findByEmail(any());
        verify(customUserRepository, times(1)).findByUsername(any());
        verify(customUserRepository, times(1)).save(any());
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_CustomUser_update() {
        when(passwordEncoder.encode(any())).thenReturn("Pistike1*");
        when(customUserRepository.findByUsername("pistike")).thenReturn(customUser1);

        when(modelMapper.map(customUserForm2, CustomUser.class)).thenReturn(customUser1);
        when(modelMapper.map(customUser1, CustomUserInfo.class)).thenReturn(customUserInfo1);

        assertEquals(customUserInfo1, customUserService.update("pistike", customUserForm2));

        verify(customUserRepository, times(2)).findByUsername(any());
        verify(customUserRepository, times(1)).findByEmail(any());
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_RegisterCustomerUser_withExistingUsername() {
        when(customUserRepository.findByUsername("pistike")).thenReturn(customUser1);

        try {
            customUserService.register(customUserForm1);
            fail("Expected UsernameExistsException, but no exception was thrown.");
        } catch (UsernameExistsException e) {
            assertEquals("Username exists with: pistike", e.getMessage());
        }
    }


    @Test
    void test_RegisterCustomerUser_withExistingEmailAddress() {
        when(customUserRepository.findByEmail("pistike@gmail.com")).thenReturn(customUser1);

        try {
            customUserService.register(customUserForm1);
            fail("Expected EmailAddressExistsException, but no exception was thrown.");
        } catch (EmailAddressExistsException e) {
            assertEquals("Email address exists with: pistike@gmail.com", e.getMessage());
        }
    }


    @Test
    void test_CustomUserActivationIsTRue() {
        when(customUserRepository.findByActivation(confirmationToken1.getConfirmationToken())).thenReturn(customUser1);

        assertEquals("Activation is successful!", customUserService.userActivation(confirmationToken1.getConfirmationToken()));

        verify(customUserRepository, times(1)).findByActivation(confirmationToken1.getConfirmationToken());
        verifyNoMoreInteractions(customUserRepository);
    }


    @Test
    void test_CustomUserActivationIsNotTrue() {
        when(customUserRepository.findByActivation(confirmationToken2.getConfirmationToken())).thenReturn(customUser2);

        assertEquals("The token is invalid or broken", customUserService.userActivation(confirmationToken2.getConfirmationToken()));

        verify(customUserRepository, times(1)).findByActivation(confirmationToken2.getConfirmationToken());
        verify(customUserRepository, times(1)).delete(customUser2);
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_LoadCustomUserByUsername() {
        when(customUserRepository.findByUsername("pistike")).thenReturn(customUser1);

        assertEquals(customUserLoggedIn1, customUserService.loadUserByUsername("pistike"));

        verify(customUserRepository, times(1)).findByUsername("pistike");
        verifyNoMoreInteractions(customUserRepository);
    }


    @Test
    void test_List_getAllCustomUsersWithOneCustomUser() {
        when(modelMapper.map(customUser1, CustomUserInfo.class)).thenReturn(customUserInfo1);
        when(customUserRepository.findAll()).thenReturn(List.of(customUser1));

        assertThat(customUserService.getCustomUsers())
                .hasSize(1)
                .containsExactly(customUserInfo1);

        verify(customUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_List_allCustomUsersWithTwoCustomUser() {
        when(customUserRepository.findAll()).thenReturn(List.of(customUser1, customUser2));
        when(modelMapper.map(customUser1, CustomUserInfo.class)).thenReturn(customUserInfo1);
        when(modelMapper.map(customUser2, CustomUserInfo.class)).thenReturn(customUserInfo2);
        assertEquals(List.of(customUserInfo1, customUserInfo2), customUserService.getCustomUsers());

        verify(customUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_FindCustomUserByUsername_withNoUsername() {
        when(customUserRepository.findByUsername("pistike")).thenReturn(null);

        try {
            customUserService.findCustomUserByUsername("pistike");
            fail("Expected UsernameNotFoundExceptionImp, but no exception was thrown.");
        } catch (UsernameNotFoundExceptionImp e) {
            assertEquals("Username was not found with: pistike", e.getMessage());
        }
    }

    @Test
    void test_FindCustomUserByUsername_withUsernamePistike() {
        when(customUserRepository.findByUsername("pistike")).thenReturn(customUser1);

        assertEquals(customUser1, customUserService.findCustomUserByUsername("pistike"));

        verify(customUserRepository, times(1)).findByUsername("pistike");
        verifyNoMoreInteractions(customUserRepository);
    }


    @Test
    void test_FindCustomUserByUsername_with2LId() {
        when(customUserRepository.findByUsername("moricka")).thenReturn(customUser2);
        assertEquals(customUser2, customUserService.findCustomUserByUsername("moricka"));

        verify(customUserRepository, times(1)).findByUsername("moricka");
        verifyNoMoreInteractions(customUserRepository);
    }


    @Test
    void test_FindCustomUserByEmail_withNoEmailAddress() {
        when(customUserRepository.findByUsername("pistike@gmail.com")).thenReturn(null);

        try {
            customUserService.findCustomUserByEmail("pistike@gmail.com");
            fail("Expected EmailAddressNotFoundException, but no exception was thrown.");
        } catch (EmailAddressNotFoundException e) {
            assertEquals("Email address was not found with: pistike@gmail.com", e.getMessage());
        }
    }

    @Test
    void test_FindCustomUserByUsername_with1LId() {
        when(customUserRepository.findByEmail("pistike@gmail.com")).thenReturn(customUser1);

        assertEquals(customUser1, customUserService.findCustomUserByEmail("pistike@gmail.com"));

        verify(customUserRepository, times(1)).findByEmail("pistike@gmail.com");
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_FindCustomUserByEmail_with2LId() {
        when(customUserRepository.findByEmail("moricka@gmail.com")).thenReturn(customUser2);
        assertEquals(customUser2, customUserService.findCustomUserByEmail("moricka@gmail.com"));

        verify(customUserRepository, times(1)).findByEmail("moricka@gmail.com");
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_CustomUser_saleWithExistingId() {
        when(customUserRepository.findByUsername("pistike")).thenReturn(customUser1);

        assertEquals("Congratulate! You sold your property!", customUserService.userSale("pistike", 1L));

        verify(customUserRepository, times(1)).findByUsername("pistike");
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_CustomUser_saleWithWrongPropertyId() {
        when(customUserRepository.findByUsername("pistike")).thenReturn(customUser1);

        assertEquals("There is no property with that id.", customUserService.userSale("pistike", 3L));

        verify(customUserRepository, times(1)).findByUsername("pistike");
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_CustomUser_deleteWithExistingId() {
        when(customUserRepository.findByUsername("pistike")).thenReturn(customUser1);

        assertEquals("You deleted your property!", customUserService.userDelete("pistike", 1L));

        verify(customUserRepository, times(1)).findByUsername("pistike");
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_CustomUser_deleteWithWrongPropertyId() {
        when(customUserRepository.findByUsername("pistike")).thenReturn(customUser1);

        assertEquals("There is no property with that id.", customUserService.userDelete("pistike", 3L));

        verify(customUserRepository, times(1)).findByUsername("pistike");
        verifyNoMoreInteractions(customUserRepository);
    }

    @Test
    void test_CustomUser_makeInactive() {
        when(customUserRepository.findByUsername("pistike")).thenReturn(customUser1);

        assertEquals("You deleted your property!", customUserService.userDelete("pistike", 1L));
        assertEquals("You deleted your profile!", customUserService.makeInactive("pistike"));

        verify(customUserRepository, times(3)).findByUsername("pistike");
        verifyNoMoreInteractions(customUserRepository);
    }



}