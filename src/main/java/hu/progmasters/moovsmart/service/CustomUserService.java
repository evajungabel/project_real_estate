package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.ConfirmationToken;
import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.domain.PropertyStatus;
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
import java.util.*;
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
        } else if (customUserRepository.findByUsername(command.getUsername()) != null) {
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
                    .confirmationToken(confirmationToken)
                    .build();
            confirmationToken.setCustomUser(customUser);
            customUserRepository.save(customUser);
            deleteIfItIsNotActivated(customUser);
        }
    }

    public void deleteIfItIsNotActivated(CustomUser customUser){
                TimerTask task = new TimerTask() {
                public void run() {
                    if (!customUser.isEnabled()) {
                        customUserRepository.delete(customUser);
                    }
                }
            };
            Timer timer = new Timer("Timer");
            long delay = 60000L;
            timer.schedule(task, delay);
        }


    public ConfirmationToken createConfirmationToken() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setCreatedDate(LocalDateTime.now());
        confirmationToken.setExpiredDate(LocalDateTime.now().plusMinutes(1));
        return confirmationTokenService.save(confirmationToken);

    }

    public String userActivation(String confirmationToken) {
        try {
            CustomUser customUser = customUserRepository.findByActivation(confirmationToken);
            if ((LocalDateTime.now()).isBefore(customUser.getConfirmationToken().getExpiredDate())) {
                customUser.setEnable(true);
                customUser.setActivation("");
                return "Activation is successful!";
            } else {
                customUserRepository.delete(customUser);
                return "The token is invalid or broken";
            }
        } catch (TokenCannotBeUsedException e){
            throw  new TokenCannotBeUsedException(confirmationToken);
        }
    }


    public void save(CustomUser customUser) {
        customUserRepository.save(customUser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            CustomUser customUser = customUserRepository.findByUsername(username);
            String[] roles = customUser.getRoles().stream()
                    .map(Enum::toString)
                    .toArray(String[]::new);

            return User
                    .withUsername(customUser.getUsername())
                    .authorities(AuthorityUtils.createAuthorityList(roles))
                    .password(customUser.getPassword())
                    .build();
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("username not found");

        }
    }

    public List<CustomUserInfo> getCustomUsers() {
        List<CustomUser> customUsers = customUserRepository.findAll();
        List<CustomUserInfo> customUserInfos = customUsers.stream()
                .map(customUser -> modelMapper.map(customUser, CustomUserInfo.class))
                .collect(Collectors.toList());
        return customUserInfos;
    }

    public void userSale(String username, Long pId) {
        CustomUser customUser = findCustomUserByUsername(username);
        for (Property property : customUser.getPropertyList()) {
            if (property.getId().equals(pId)) {
                property.setStatus(PropertyStatus.INACTIVE);
                property.setDateOfSale(LocalDateTime.now());
            }
        }
    }

    public CustomUser findCustomUserByUsername(String username) {
        Optional<CustomUser> customUserOptional = Optional.ofNullable(customUserRepository.findByUsername(username));
        if (customUserOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return customUserOptional.get();
    }

    public void userDelete(String username, Long pId) {
        CustomUser customUser = findCustomUserByUsername(username);
        for (Property property : customUser.getPropertyList()) {
            if (property.getId().equals(pId)) {
                property.setStatus(PropertyStatus.INACTIVE);
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

    public CustomUserInfo update(String username, CustomUserForm customUserForm) {
        CustomUser customUser = findCustomUserByUsername(username);
        if (customUserRepository.findByEmail(customUserForm.getEmail()) != null &&
                customUserRepository.findByEmail(customUserForm.getEmail()) != customUser) {
            throw new EmailAddressExistsException(customUserForm.getEmail());
        } else if (customUserRepository.findByUsername(customUserForm.getUsername()) != null &&
                customUserRepository.findByUsername(customUserForm.getUsername()) != customUser) {
            throw new UsernameExistsException(customUserForm.getUsername());
        } else {
            modelMapper.map(customUserForm, customUser);
            customUser.setPassword(passwordEncoder.encode(customUserForm.getPassword()));
            return modelMapper.map(customUser, CustomUserInfo.class);
        }
    }

    public void makeInactive(String customUsername) {
        CustomUser toDelete = findCustomUserByUsername(customUsername);
        userDelete(toDelete.getUsername(), toDelete.getCustomUserId());
        toDelete.setDeleteDate(LocalDateTime.now());
        toDelete.setDeleted(true);
        toDelete.setEmail(null);
        toDelete.setName(null);
        toDelete.setPassword(null);
        toDelete.setUsername(null);
    }
}
