package hu.progmasters.moovsmart.validation;

import hu.progmasters.moovsmart.dto.UserForm;
import hu.progmasters.moovsmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserFormValidator implements Validator {

    private UserRepository userRepository;

    @Autowired
    public UserFormValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserForm user = (UserForm) o;
        if (user.getName() == null || user.getName().equals("")) {
            errors.rejectValue("name", "user.name.empty");
        }
    }



}
