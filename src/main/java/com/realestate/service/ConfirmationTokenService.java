package com.realestate.service;

import com.realestate.domain.ConfirmationToken;
import com.realestate.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public ConfirmationToken save(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }
}
