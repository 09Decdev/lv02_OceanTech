package com.globits.da.exception;

public class CommuneNotFoundException extends RuntimeException{
    public CommuneNotFoundException(Long id) {
        super(ErrorCode.NOT_FOUND_COMMUNE.getMessage()+id);
    }
}
