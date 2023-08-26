package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.dto.CustomUserForm;
import hu.progmasters.moovsmart.exception.CustomUserNotFoundException;
import hu.progmasters.moovsmart.repository.CustomUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class CustomUserService {

    private CustomUserRepository customUserRepository;

    private ModelMapper modelMapper;


    @Autowired
    public CustomUserService(CustomUserRepository customUserRepository, ModelMapper modelMapper) {
        this.customUserRepository = customUserRepository;
        this.modelMapper = modelMapper;
    }


    public void userDelete(Long cId, Long pId) {
        CustomUser customUser = findCustomUserById(cId);
        for (Property property : customUser.getPropertyList()) {
            if (property.getId().equals(pId)) {
                property.setActive(false);
                property.setDateOfInactivation(LocalDateTime.now());
            }
        }
    }

    private CustomUser findCustomUserById(Long cId) {
        Optional<CustomUser> customUserOptional = customUserRepository.findById(cId);
        if (customUserOptional.isEmpty()) {
            throw new CustomUserNotFoundException(cId);
        }
        return customUserOptional.get();
    }


    public void save(CustomUserForm command) {
        CustomUser toSave = modelMapper.map(command, CustomUser.class);
        customUserRepository.save(toSave);
    }

    public void userSale(Long cId, Long pId) {
        CustomUser customUser = findCustomUserById(cId);
        for (Property property : customUser.getPropertyList()) {
            if (property.getId().equals(pId)) {
                property.setActive(false);
                property.setDateOfSale(LocalDateTime.now());
            }
        }
    }
}
