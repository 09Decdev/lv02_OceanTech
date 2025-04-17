package com.globits.da.exception;

public class CertificateNotValidsException extends RuntimeException {
    public CertificateNotValidsException() {
        super(ErrorCode.CHECK_DATE_CERTIFICATE.getMessage());
    }
}
