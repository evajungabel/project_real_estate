package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.ConfirmationToken;
import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.dto.CustomUserForm;
import hu.progmasters.moovsmart.dto.CustomUserInfo;
import hu.progmasters.moovsmart.exception.EmailAddressExistsException;
import hu.progmasters.moovsmart.exception.EmailAddressNotFoundException;
import hu.progmasters.moovsmart.exception.TokenCannotBeUsedException;
import hu.progmasters.moovsmart.exception.UsernameExistsException;
import hu.progmasters.moovsmart.repository.CustomUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomUserService implements UserDetailsService {

    private final CustomUserRepository customUserRepository;

    private ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private ConfirmationTokenService confirmationTokenService;


    @Autowired
    public CustomUserService(CustomUserRepository customUserRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService) {
        this.customUserRepository = customUserRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }


    public void register(CustomUserForm command) {
        if (customUserRepository.findByEmail(command.getEmail()) != null) {
            throw new EmailAddressExistsException(command.getEmail());
        } else if (customUserRepository.findById(command.getUsername()).isPresent()) {
            throw new UsernameExistsException(command.getUsername());
        } else {
            ConfirmationToken confirmationToken = createConfirmationToken();
            CustomUser customUser = new CustomUser().builder()
                    .username(command.getUsername())
                    .name(command.getName())
                    .email(command.getEmail())
                    .password(passwordEncoder.encode(command.getPassword()))
                    .roles(List.of(CustomUserRole.ROLE_USER))
                    .enable(false)
                    .activation(confirmationToken.getConfirmationToken())
                    .build();
            confirmationToken.setCustomUser(customUser);
            customUserRepository.save(customUser);
        }
    }

    public ConfirmationToken createConfirmationToken() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setCreatedDate(LocalDateTime.now());
        confirmationToken.setExpiredDate(LocalDateTime.now().plusMinutes(1));
        return confirmationTokenService.save(confirmationToken);

    }

    public String userActivation(String confirmationToken) {
        CustomUser customUser = customUserRepository.findByActivation(confirmationToken);
        if (calculateMinutesBetweenLocalDates(customUser.getConfirmationToken().getCreatedDate(), customUser.getConfirmationToken().getExpiredDate()) <= 1) {
            customUser.setEnable(true);
            customUser.setActivation("");
            return "Activation is successful!";
        } else {
            throw new TokenCannotBeUsedException(confirmationToken);
        }
    }

    public long calculateMinutesBetweenLocalDates(LocalDateTime ld1, LocalDateTime ld2) {
        return Math.abs(ChronoUnit.MINUTES.between(ld1, ld2)) + 1;
    }


    public void save(CustomUser customUser) {
        customUserRepository.save(customUser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser customUser = customUserRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        String[] roles = customUser.getRoles().stream()
                .map(Enum::toString)
                .toArray(String[]::new);

        return User
                .withUsername(customUser.getUsername())
                .authorities(AuthorityUtils.createAuthorityList(roles))
                .password(customUser.getPassword())
                .build();
    }

    public List<CustomUserInfo> getCustomUsers() {
        List<CustomUser> customUsers = customUserRepository.findAll();
        List<CustomUserInfo> customUserInfos = customUsers.stream()
                .map(customUser -> modelMapper.map(customUser, CustomUserInfo.class))
                .collect(Collectors.toList());
        return customUserInfos;
    }

    public void userSale(String username, Long pId) {
        CustomUser customUser = findCustomUserById(username);
        for (Property property : customUser.getPropertyList()) {
            if (property.getId().equals(pId)) {
                property.setActive(false);
                property.setDateOfSale(LocalDateTime.now());
            }
        }
    }

    public CustomUser findCustomUserById(String username) {
        Optional<CustomUser> customUserOptional = customUserRepository.findById(username);
        if (customUserOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return customUserOptional.get();
    }


    public void userDelete(String username, Long pId) {
        CustomUser customUser = findCustomUserById(username);
        for (Property property : customUser.getPropertyList()) {
            if (property.getId().equals(pId)) {
                property.setActive(false);
                property.setDateOfInactivation(LocalDateTime.now());
            }
        }
    }

    public CustomUser findCustomUserByEmail(String email) {
        Optional<CustomUser> customUserOptional = Optional.ofNullable(customUserRepository.findByEmail(email));
        if (customUserOptional.isEmpty()) {
            throw new EmailAddressNotFoundException(email);
        }
        return customUserOptional.get();
    }

}
