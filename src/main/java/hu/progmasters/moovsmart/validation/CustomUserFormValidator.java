//package hu.progmasters.moovsmart.validation;
//
//import hu.progmasters.moovsmart.dto.CustomUserForm;
//import hu.progmasters.moovsmart.repository.CustomUserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//
//@Component
//public class CustomUserFormValidator implements Validator {
//
//    private CustomUserRepository userRepository;
//
//    @Autowired
//    public CustomUserFormValidator(CustomUserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return CustomUserForm.class.equals(aClass);
//    }
//
//    @Override
//    public void validate(Object o, Errors errors) {
//        CustomUserForm user = (CustomUserForm) o;
//        if (user.getUserName() == null || user.getUserName().equals("")) {
//            errors.rejectValue("name", "user.name.empty");
//        }
//    }
//
//}
