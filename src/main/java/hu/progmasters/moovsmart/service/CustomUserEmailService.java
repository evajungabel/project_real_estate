package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.CustomUserEmail;
import hu.progmasters.moovsmart.repository.CustomUserEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomUserEmailService {

    private CustomUserEmailRepository customUserEmailRepository;

    @Autowired
    public CustomUserEmailService(CustomUserEmailRepository customUserEmailRepository) {
        this.customUserEmailRepository = customUserEmailRepository;
    }

    public void save(CustomUserEmail customUserEmail) {
        customUserEmailRepository.save(customUserEmail);
    }

    public void delete(CustomUserEmail customUserEmail) {
        customUserEmailRepository.delete(customUserEmail);
    }

    public List<CustomUserEmail> getCustomUserEmails() {
        return customUserEmailRepository.findAll();
    }
}
