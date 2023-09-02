package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.dto.ConfirmationToken;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomUserService implements UserDetailsService {

    private final CustomUserRepository customUserRepository;

    private ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserService(CustomUserRepository customUserRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.customUserRepository = customUserRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }



    public void register(CustomUserForm command) {
        if (customUserRepository.findByEmail(command.getEmail()) != null) {
            throw new EmailAddressExistsException(command.getEmail());
        } else if (customUserRepository.findById(command.getUsername()).isPresent()) {
            throw new UsernameExistsException(command.getUsername());
        } else {
            CustomUser customUser = new CustomUser()
                    .setUsername(command.getUsername())
                    .setName(command.getName())
                    .setEmail(command.getEmail())
                    .setPassword(passwordEncoder.encode(command.getPassword()))
                    .setRoles(List.of(CustomUserRole.ROLE_USER))
                    .setEnable(false)
                    .setActivation((new ConfirmationToken()).getConfirmationToken());
            customUserRepository.save(customUser);
        }
    }

    public String userActivation(String confirmationToken){
        CustomUser customUser = customUserRepository.findByConfirmationToken(confirmationToken);
        try{
            customUser.setEnable(true);
            customUser.setActivation("");
            customUserRepository.save(customUser); //Kell-e?
            return "ok";
        } catch (TokenCannotBeUsedException e){
            throw  new TokenCannotBeUsedException(confirmationToken);
        }
    }

    public void save(CustomUser customUser){
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
