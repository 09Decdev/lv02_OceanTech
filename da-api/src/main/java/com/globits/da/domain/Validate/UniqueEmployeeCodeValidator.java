package com.globits.da.domain.Validate;

import com.globits.da.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
@Component
public class UniqueEmployeeCodeValidator implements ConstraintValidator<UniqueEmployeeCode,String> {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {
       if (code == null||code.trim().isEmpty()) {
           return true;
       }
       return !employeeRepository.existsByCode(code);
    }

    @Override
    public void initialize(UniqueEmployeeCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
