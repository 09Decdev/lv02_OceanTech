package com.globits.da.exception;

public class CertificateNotValidException extends RuntimeException{
    public CertificateNotValidException(String certificate) {
        super("Employee already has 3 valid certificates of type"+certificate);
    }
}
