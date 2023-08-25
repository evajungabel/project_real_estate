package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.User;
import hu.progmasters.moovsmart.dto.UserForm;
import hu.progmasters.moovsmart.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public void save(UserForm command) {
        User toSave = modelMapper.map(command, User.class);
        userRepository.save(toSave);
    }
}
