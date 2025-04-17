package com.globits.da.exception;

public class DistrictNotFoundException extends RuntimeException {
    public DistrictNotFoundException(Long id) {
        super(ErrorCode.NOT_FOUND_DISTRICT.getMessage()+id);
    }
}
