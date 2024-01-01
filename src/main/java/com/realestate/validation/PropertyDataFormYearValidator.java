package com.realestate.validation;


import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Component
public class PropertyDataFormYearValidator implements ConstraintValidator<NotInFuture, Integer> {


    @Override
    public boolean isValid(Integer yearBuilt, ConstraintValidatorContext context) {
        if (yearBuilt == null) {
            return true;
        }

        int currentYear = LocalDate.now().getYear();
        return yearBuilt <= currentYear;
    }
}