package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.dto.CustomUserForm;
import hu.progmasters.moovsmart.dto.CustomUserInfo;
import hu.progmasters.moovsmart.exception.EmailAddressExistsException;
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
                    .setRoles(List.of(CustomUserRole.ROLE_USER));
            customUserRepository.save(customUser);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
