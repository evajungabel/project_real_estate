package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.domain.EstateAgent;
import hu.progmasters.moovsmart.dto.CustomUserInfo;
import hu.progmasters.moovsmart.repository.EstateAgentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class EstateAgentService {
    private EstateAgentRepository estateAgentRepository;
    private ModelMapper modelMapper;

    public EstateAgentService(EstateAgentRepository estateAgentRepository, ModelMapper modelMapper) {
        this.estateAgentRepository = estateAgentRepository;
        this.modelMapper = modelMapper;
    }


    public CustomUserInfo save(CustomUser customUser) {
        EstateAgent toSave = new EstateAgent();
        toSave.setCustomUser(customUser);
        estateAgentRepository.save(toSave);
        return modelMapper.map(customUser, CustomUserInfo.class);
    }
}
