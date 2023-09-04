package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.CustomUserDeleted;
import hu.progmasters.moovsmart.repository.CustomUserDeletedRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CustomUserDeletedService {

    private CustomUserDeletedRepository customUserDeletedRepository;

    public CustomUserDeletedService(CustomUserDeletedRepository customUserDeletedRepository) {
        this.customUserDeletedRepository = customUserDeletedRepository;
    }

    public void save(CustomUserDeleted customUserDeleted){
        customUserDeletedRepository.save(customUserDeleted);
    }
}
