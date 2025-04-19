package com.globits.da.exception;

import org.springframework.http.HttpStatus;

//@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    CHECK_DATE_CERTIFICATE(1007, "Issue date must not be after expiry date.", HttpStatus.BAD_REQUEST),
    CODE_EMPLOYEE_EXISTED(1008, "Employee code already exists", HttpStatus.BAD_REQUEST),
    Not_Found_EMPLOYEE(1009, "Employee not found with id:", HttpStatus.NOT_FOUND),
    NOT_FOUND_CERTIFICATE(1009, "Certificate not found with id: ", HttpStatus.NOT_FOUND),
    NOT_FOUND_DISTRICT(1009, "District not found with id: ", HttpStatus.NOT_FOUND),
    NOT_FOUND_COMMUNE(1009, "Commune not found with id: ", HttpStatus.NOT_FOUND),
    NOT_FOUND_TYPE(1009, "Certificate Type not found with id: ", HttpStatus.NOT_FOUND),
    NOT_FOUND_PROVINCE(1009, "Province not found with id: ", HttpStatus.NOT_FOUND),
    BAD_REQUEST (4000, "Bad Request",HttpStatus.BAD_REQUEST),
    INVALID_KEY (4002, "Dữ liệu không hợp lệ hoặc bị trùng",HttpStatus.BAD_REQUEST)
    ;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
