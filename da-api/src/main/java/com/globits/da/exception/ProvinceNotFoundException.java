package com.globits.da.exception;

public class ProvinceNotFoundException extends RuntimeException{
    public ProvinceNotFoundException(Long id) {
        super(ErrorCode.NOT_FOUND_PROVINCE.getMessage()+id);
    }
}
