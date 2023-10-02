package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.domain.CustomUserImageURL;
import hu.progmasters.moovsmart.domain.PropertyImageURL;
import hu.progmasters.moovsmart.exception.CustomUserImageURLNotFoundException;
import hu.progmasters.moovsmart.exception.PropertyNotFoundException;
import hu.progmasters.moovsmart.repository.CustomUserImageURLRepository;
import hu.progmasters.moovsmart.repository.PropertyImageURLRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CustomUserImageURLService {

    private CustomUserImageURLRepository customUserImageURLRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CustomUserImageURLService(CustomUserImageURLRepository customUserImageURLRepository, ModelMapper modelMapper) {
        this.customUserImageURLRepository = customUserImageURLRepository;
        this.modelMapper = modelMapper;
    }

    public CustomUserImageURL save(CustomUserImageURL toSave) {
        return customUserImageURLRepository.save(toSave);
    }


    public CustomUserImageURL findCustomUserImageURLById(Long customUserImageURLId) {
        Optional<CustomUserImageURL> customUserImageURLOptional = customUserImageURLRepository.findById(customUserImageURLId);
        if (customUserImageURLOptional.isEmpty()) {
            throw new CustomUserImageURLNotFoundException(customUserImageURLId);
        }
        return customUserImageURLOptional.get();
    }

    public void deleteById(Long customUserImageURLId) {
        customUserImageURLRepository.deleteById(customUserImageURLId);
    }
}
