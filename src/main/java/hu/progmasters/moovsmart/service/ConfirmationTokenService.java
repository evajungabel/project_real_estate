package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.domain.CustomUser;
import hu.progmasters.moovsmart.dto.ConfirmationToken;
import hu.progmasters.moovsmart.dto.CustomUserForm;
import hu.progmasters.moovsmart.exception.TokenCannotBeUsedException;
import hu.progmasters.moovsmart.repository.ConfirmationTokenRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ConfirmationTokenService {

    private ConfirmationTokenRepository confirmationTokenRepository;

    private ModelMapper modelMapper;

    private CustomUserService customUserService;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, ModelMapper modelMapper, CustomUserService customUserService) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.modelMapper = modelMapper;
        this.customUserService = customUserService;
    }




    public void save(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public void findByConfirmationToken(ConfirmationToken confirmationToken) {
        if (confirmationToken != null) {
            CustomUser customUser = customUserService.findCustomUserByEmail(confirmationToken.getCustomUser().getEmail());
            customUser.setEnable(true);
            customUserService.save(customUser);
        } else {
            throw new TokenCannotBeUsedException(confirmationToken.getTokenId());
        }
    }

//    public ConfirmationToken getToken(CustomUserForm command) {
//        return new ConfirmationToken(modelMapper.map(command, CustomUser.class));
//    }
}
