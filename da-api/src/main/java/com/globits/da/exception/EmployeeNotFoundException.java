package com.globits.da.exception;

public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException(Long id) {
        super(ErrorCode.Not_Found_EMPLOYEE.getMessage()+id);
    }
}
