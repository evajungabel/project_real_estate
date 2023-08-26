package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.dto.CustomUserForm;
import hu.progmasters.moovsmart.repository.CustomUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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



    public void save(CustomUserForm command) {
        CustomUser toSave = modelMapper.map(command, CustomUser.class);
        customUserRepository.save(toSave);
    }
}
