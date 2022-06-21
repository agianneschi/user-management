package com.example.usermanagement.util;

import com.example.usermanagement.dto.UserDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidationUtils {

    public static String validateUserDto(UserDto userDto) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        String validationErr = "";
        for (ConstraintViolation<UserDto> violation : violations) {
            String viol = violation.getMessage();
            validationErr = validationErr + viol;
        }

        return validationErr;
    }
}
