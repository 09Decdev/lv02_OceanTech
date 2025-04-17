package com.globits.da.exception;

public class EmployeeCodeExistsException extends RuntimeException{
    public EmployeeCodeExistsException(String code) {
        super(ErrorCode.CODE_EMPLOYEE_EXISTED.getMessage() + code);
    }
}
