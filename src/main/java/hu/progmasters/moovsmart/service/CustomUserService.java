package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.*;
import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.*;
import hu.progmasters.moovsmart.repository.AgentCommentRepository;
import hu.progmasters.moovsmart.repository.CustomUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class CustomUserService implements UserDetailsService {

    private final CustomUserRepository customUserRepository;
    private ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private ConfirmationTokenService confirmationTokenService;
    private EstateAgentService estateAgentService;
    private AgentCommentRepository agentCommentRepository;

    private List<String> listOfEmails = new ArrayList<>();
    private List<String> listOfUsernames = new ArrayList<>();

    @Autowired
    public CustomUserService(CustomUserRepository customUserRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService, EstateAgentService estateAgentService, AgentCommentRepository agentCommentRepository) {
        this.customUserRepository = customUserRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.estateAgentService = estateAgentService;
        this.agentCommentRepository = agentCommentRepository;
    }

    public CustomUserInfo register(CustomUserForm command) {
        if (customUserRepository.findByEmail(command.getEmail()) != null) {
            throw new EmailAddressExistsException(command.getEmail());
        } else if (customUserRepository.findByUsername(command.getUsername()) != null) {
            throw new UsernameExistsException(command.getUsername());
        } else {
            ConfirmationToken confirmationToken = createConfirmationToken();
            CustomUser customUser = new CustomUser().builder()
                    .username(command.getUsername())
                    .name(command.getName())
                    .email(command.getEmail())
                    .password(passwordEncoder.encode(command.getPassword()))
                    .roles(List.of(CustomUserRole.ROLE_USER))
                    .enable(false)
                    .activation(confirmationToken.getConfirmationToken())
                    .confirmationToken(confirmationToken)
                    .isAgent(command.getIsAgent())
                    .build();
            confirmationToken.setCustomUser(customUser);
            deleteIfItIsNotActivated(customUser);
            customUserRepository.save(customUser);
            if (customUser.isAgent()) {
                customUser.setRoles(List.of(CustomUserRole.ROLE_AGENT));
                estateAgentService.save(customUser);
            }
            return modelMapper.map(customUser, CustomUserInfo.class);
        }
    }

    public void deleteIfItIsNotActivated(CustomUser customUser) {
        TimerTask task = new TimerTask() {
            public void run() {
                if (!customUser.isEnabled()) {
                    customUserRepository.delete(customUser);
                }
            }
        };
        Timer timer = new Timer("Timer");
        long delay = 1000000L;
        timer.schedule(task, delay);
    }


    public ConfirmationToken createConfirmationToken() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setCreatedDate(LocalDateTime.now());
        confirmationToken.setExpiredDate(LocalDateTime.now().plusMinutes(2));
        return confirmationTokenService.save(confirmationToken);
    }

    public String userActivation(String confirmationToken) {
        try {
            CustomUser customUser = customUserRepository.findByActivation(confirmationToken);
            if ((LocalDateTime.now()).isBefore(customUser.getConfirmationToken().getExpiredDate())) {
                customUser.setEnable(true);
                customUser.setActivation("");
                return "Activation is successful!";
            } else {
                customUserRepository.delete(customUser);
                return "The token is invalid or broken";
            }
        } catch (TokenCannotBeUsedException e) {
            throw new TokenCannotBeUsedException(confirmationToken);
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            CustomUser customUser = customUserRepository.findByUsername(username);
            String[] roles = customUser.getRoles().stream()
                    .map(Enum::toString)
                    .toArray(String[]::new);

            return User
                    .withUsername(customUser.getUsername())
                    .authorities(AuthorityUtils.createAuthorityList(roles))
                    .password(customUser.getPassword())
                    .build();
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("username not found");

        }
    }

    public List<CustomUserInfo> getCustomUsers() {
        List<CustomUser> customUsers = customUserRepository.findAll();
        List<CustomUserInfo> customUserInfos = customUsers.stream()
                .map(customUser -> modelMapper.map(customUser, CustomUserInfo.class))
                .collect(Collectors.toList());
        return customUserInfos;
    }

    public CustomUser findCustomUserByUsername(String username) {
        Optional<CustomUser> customUserOptional = Optional.ofNullable(customUserRepository.findByUsername(username));
        if (customUserOptional.isEmpty()) {
            throw new UsernameNotFoundExceptionImp(username);
        }
        return customUserOptional.get();
    }

    public CustomUser findCustomUserByEmail(String email) {
        Optional<CustomUser> customUserOptional = Optional.ofNullable(customUserRepository.findByEmail(email));
        if (customUserOptional.isEmpty()) {
            throw new EmailAddressNotFoundException(email);
        }
        return customUserOptional.get();
    }


    public String userSale(String username, Long pId) {
        CustomUser customUser = findCustomUserByUsername(username);
        for (Property property : customUser.getPropertyList()) {
            if (property.getId().equals(pId)) {
                property.setStatus(PropertyStatus.INACTIVE);
                property.setDateOfSale(LocalDateTime.now());
                if (customUser.getRoles().equals(List.of(CustomUserRole.ROLE_AGENT))) {
                    Integer sellPoint = customUser.getEstateAgent().getSellPoint();
                    customUser.getEstateAgent().setSellPoint(sellPoint + 1);
                    if (sellPoint >= 50 && sellPoint < 150) {
                        customUser.getEstateAgent().setRank(AgentRank.MEDIOR);
                    } else if (sellPoint >= 150) {
                        customUser.getEstateAgent().setRank(AgentRank.PROFESSIONAL);
                    } else {
                        customUser.getEstateAgent().setRank(AgentRank.RECRUIT);
                    }
                }
                return "Congratulate! You sold your property!";
            }
        }
        return "There is no property with that id.";
    }


    public String userDelete(String username, Long pId) {
        CustomUser customUser = findCustomUserByUsername(username);
        for (Property property : customUser.getPropertyList()) {
            if (property.getId().equals(pId)) {
                property.setStatus(PropertyStatus.INACTIVE);
                property.setDateOfInactivation(LocalDateTime.now());
                return "You deleted your property!";
            }
        }
        return "There is no property with that id.";
    }


    public CustomUserInfo update(String username, CustomUserForm customUserForm) {
        CustomUser customUser = findCustomUserByUsername(username);
        if (customUserRepository.findByUsername(customUserForm.getUsername()) != null &&
                !(customUserForm.getUsername().equals(customUser.getUsername()))) {
            throw new UsernameExistsException(customUserForm.getUsername());
        } else if (customUserRepository.findByEmail(customUserForm.getEmail()) != null &&
                !(customUserForm.getEmail().equals(customUser.getEmail()))) {
            throw new EmailAddressExistsException(customUserForm.getEmail());
        } else {
            modelMapper.map(customUserForm, customUser);
            customUser.setPassword(passwordEncoder.encode(customUserForm.getPassword()));
            return modelMapper.map(customUser, CustomUserInfo.class);
        }
    }

    public String makeInactive(String customUsername) {
        CustomUser toDelete = findCustomUserByUsername(customUsername);
        userDelete(toDelete.getUsername(), toDelete.getCustomUserId());
        toDelete.setDeleteDate(LocalDateTime.now());
        toDelete.setDeleted(true);
        toDelete.setEmail(null);
        toDelete.setName(null);
        toDelete.setPassword(null);
        toDelete.setUsername(null);
        return "You deleted your profile!";
    }

    public AgentCommentInfo comment(CommentForm comment) {
        CustomUser agent = findCustomUserByUsername(comment.getAgentName());
        CustomUser user = findCustomUserByUsername(comment.getUserName());
        AgentComment agentComment = new AgentComment();
        agentComment.setComment(comment.getComment());
        agentComment.setCustomUsername(user.getUsername());
        agentComment.setEstateAgent(agent.getEstateAgent());
        agentCommentRepository.save(agentComment);
        return modelMapper.map(agentComment, AgentCommentInfo.class);
    }

    public EstateAgentInfo getAgentInfo(String userName) {
        CustomUser customUser = findCustomUserByUsername(userName);
        EstateAgent agent = customUser.getEstateAgent();
        List<AgentComment> comments = agent.getComments();
        List<AgentCommentInfo> commentInfos = new ArrayList<>();
        for (AgentComment comment : comments) {
            commentInfos.add(modelMapper.map(comment, AgentCommentInfo.class));
        }
        EstateAgentInfo info = modelMapper.map(agent, EstateAgentInfo.class);
        info.setCommentInfo(commentInfos);
        return info;
    }
}
