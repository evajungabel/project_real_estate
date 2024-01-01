package com.realestate.service;

import com.realestate.domain.AgentRank;
import com.realestate.domain.CustomUser;
import com.realestate.domain.EstateAgent;
import com.realestate.dto.CustomUserInfo;
import com.realestate.repository.EstateAgentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class EstateAgentService {
    private EstateAgentRepository estateAgentRepository;
    private ModelMapper modelMapper;

    @Autowired
    public EstateAgentService(EstateAgentRepository estateAgentRepository, ModelMapper modelMapper) {
        this.estateAgentRepository = estateAgentRepository;
        this.modelMapper = modelMapper;
    }


    public CustomUserInfo save(CustomUser customUser) {
        EstateAgent toSave = new EstateAgent();
        toSave.setCustomUser(customUser);
        toSave.setRank(AgentRank.RECRUIT);
        toSave.setSellPoint(0);
        estateAgentRepository.save(toSave);
        return modelMapper.map(customUser, CustomUserInfo.class);
    }
}
