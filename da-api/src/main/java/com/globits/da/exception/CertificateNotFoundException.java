package com.globits.da.exception;

public class CertificateNotFoundException extends RuntimeException{
    public CertificateNotFoundException(Long id) {
        super(ErrorCode.NOT_FOUND_CERTIFICATE.getMessage()+id);
    }
}
