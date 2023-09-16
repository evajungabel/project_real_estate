package hu.progmasters.moovsmart.service;

import hu.progmasters.moovsmart.config.CustomUserRole;
import hu.progmasters.moovsmart.domain.*;
import hu.progmasters.moovsmart.dto.*;
import hu.progmasters.moovsmart.exception.*;
import hu.progmasters.moovsmart.repository.AgentCommentRepository;
import hu.progmasters.moovsmart.repository.CustomUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    private SendingEmailService sendingEmailService;

    private PropertyService propertyService;

    private CustomUserEmailService customUserEmailService;

    private Timer activationTimer = new Timer("Timer");

    @Autowired
    public CustomUserService(CustomUserRepository customUserRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService, EstateAgentService estateAgentService, SendingEmailService sendingEmailService, CustomUserEmailService customUserEmailService) {
        this.customUserRepository = customUserRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.estateAgentService = estateAgentService;
        this.agentCommentRepository = agentCommentRepository;
        this.sendingEmailService = sendingEmailService;
        this.customUserEmailService = customUserEmailService;
    }


    @Autowired
    public CustomUserService setPropertyService(PropertyService propertyService) {
        this.propertyService = propertyService;
        return this;
    }


    public CustomUserInfo register(CustomUserForm customUserForm) {
        if (customUserRepository.findByEmail(customUserForm.getEmail()) != null) {
            throw new EmailAddressExistsException(customUserForm.getEmail());
        } else if (customUserRepository.findByUsername(customUserForm.getUsername()) != null) {
            throw new UsernameExistsException(customUserForm.getUsername());
        } else {
            ConfirmationToken confirmationToken = createConfirmationToken();
            CustomUser customUser = new CustomUser().builder()
                    .username(customUserForm.getUsername())
                    .name(customUserForm.getName())
                    .email(customUserForm.getEmail())
                    .phoneNumber(customUserForm.getPhoneNumber())
                    .password(passwordEncoder.encode(customUserForm.getPassword()))
                    .roles(List.of(CustomUserRole.ROLE_USER))
                    .enable(false)
                    .hasNewsletter(customUserForm.getHasNewsletter())
                    .activation(confirmationToken.getConfirmationToken())
                    .confirmationToken(confirmationToken)
                    .isAgent(customUserForm.getIsAgent())
                    .build();
            confirmationToken.setCustomUser(customUser);
            if (customUser.isAgent()) {
                customUser.setRoles(List.of(CustomUserRole.ROLE_AGENT));
                estateAgentService.save(customUser);
            }
            CustomUserEmail customUserEmail = CustomUserEmail.builder()
                    .email(customUserForm.getEmail())
                    .customUser(customUser)
                    .build();
            if (customUser.isHasNewsletter()) {
                customUserEmailService.save(customUserEmail);
            }
            CustomUser savedUser = customUserRepository.save(customUser);
            deleteIfItIsNotActivated(savedUser);
            CustomUserInfo customUserInfo = modelMapper.map(savedUser, CustomUserInfo.class);
            customUserInfo.setCustomUserRoles(customUser.getRoles());
            sendingActivationEmail(customUserForm.getName(), customUserForm.getEmail());
            return customUserInfo;
        }
    }

    public void sendingActivationEmail(String name, String email) {
        String subject = "Felhasználói fiók aktivalása";
        String text = "Kedves " + name +
                "! \n \n Köszönjük, hogy regisztrált az oldalunkra! " +
                "\n \n Kérem, kattintson a linkre, hogy visszaigazolja a regisztrációját," +
                " amire 30 perce van! \n \n http://localhost:8080/api/customusers/activation/"
                + findCustomUserByEmail(email).getActivation();
        sendingEmailService.sendEmail(email, subject, text);
    }

    public void deleteIfItIsNotActivated(CustomUser customUser) {
        TimerTask task = new TimerTask() {
            public void run() {
                if (!(customUser.isEnabled())) {
                    customUserRepository.delete(customUser);
                }
            }
        };
        long delay = 60000L;
        activationTimer.schedule(task, delay);
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
                activationTimer.cancel();
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


    public CustomUserInfo getCustomUserDetails(String username) {
        CustomUser customUser = findCustomUserByUsername(username);
        return modelMapper.map(customUser, CustomUserInfo.class);
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
            String emailOld = customUser.getEmail();
            String nameOld = customUser.getName();
            modelMapper.map(customUserForm, customUser);
            customUser.setPassword(passwordEncoder.encode(customUserForm.getPassword()));
            CustomUserInfo customUserInfo = modelMapper.map(customUser, CustomUserInfo.class);
            customUserInfo.setCustomUserRoles(customUser.getRoles());
            sendingEmailForUpdate(nameOld, emailOld);
            return customUserInfo;
        }
    }

    public void sendingEmailForUpdate(String name, String email) {
        String subject = "Felhasználói fiók adatainak megváltoztatása";
        String text = "Kedves " + name +
                "! \n \n Felhasználói fiókjának adatai megváltoztak! " +
                "Ha nem Ön tette, mielőbb lépjen kapcsolatba velünk!";
        sendingEmailService.sendEmail(email, subject, text);
    }


    //Email listából hírlevélhez vegyük ki?
    public String makeInactive(String customUsername) {
        CustomUser toDelete = findCustomUserByUsername(customUsername);
        userDelete(toDelete.getUsername(), toDelete.getCustomUserId());
        toDelete.setUsername(null);
        toDelete.setName(null);
        toDelete.setEmail(null);
        toDelete.setPassword(null);
        toDelete.setPhoneNumber(null);
        toDelete.setRoles(null);
        toDelete.setEnable(false);
        toDelete.setActivation(null);
        toDelete.setConfirmationToken(null);
        toDelete.setDeleteDate(LocalDateTime.now());
        toDelete.setDeleted(true);
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


    @Scheduled(cron = "0 * * ? * *")
    public void sendingNewsletter() {
        for (CustomUserEmail customUserEmail : customUserEmailService.getCustomUserEmails()) {
            String subject = "Hírlevél az újdonságokról!";
            String text = "Kedves " + findCustomUserByEmail(customUserEmail.getEmail()).getName() +
                    "! \n \n Ezennel küldjük a 24 óra alatt regisztrált új ingatlanokat!"
                    + "\n \n" + propertyService.getProperties24().toString()
                    + "\n \n Ha le szeretne íratkozni, kérem kattintson a következő linkre: "
                    + "http://localhost:8080/api/customusers/unsubscribenewsletter/"
                    + customUserEmail.getCustomUser().getConfirmationToken().getConfirmationToken();
            sendingEmailService.sendEmail(customUserEmail.getEmail(), subject, text);
        }
    }

    public String userUnsubscribeNewsletter(String confirmationToken) {
        try {CustomUser customUser = customUserRepository.findByActivation(confirmationToken);
            customUserEmailService.delete(customUser.getCustomUserEmail());
            return "Sikeresen leíratkozott a hírlevélről!";
        } catch (TokenCannotBeUsedException e) {
            throw new TokenCannotBeUsedException(confirmationToken);
        }

    }
}
