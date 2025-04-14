package com.globits.da.domain.Validate;

import com.globits.da.dto.request.CertificateRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class CertificateDateValidator implements ConstraintValidator<ValidCertificateDates, CertificateRequestDto> {

    @Override
    public void initialize(ValidCertificateDates constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CertificateRequestDto certificateRequestDto, ConstraintValidatorContext constraintValidatorContext) {
        if (certificateRequestDto == null) {
            return true;
        }
        LocalDate issueDate=certificateRequestDto.getIssueDate();
        LocalDate expiryDate=certificateRequestDto.getExpiryDate();

        if (issueDate==null||expiryDate==null){
            return true;
        }

        return !issueDate.isAfter(expiryDate);
    }
}
