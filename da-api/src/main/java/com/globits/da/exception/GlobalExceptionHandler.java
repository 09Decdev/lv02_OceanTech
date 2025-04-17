package com.globits.da.exception;

import com.globits.da.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Component
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleExceptions(Exception ex) {
        String errorMessage = ex.getMessage() + ex.getClass().getSimpleName();

        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.BAD_REQUEST.getCode());
        apiResponse.setMessage(errorMessage);
        apiResponse.setData(null);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("RuntimeException: {}", ex.getMessage());
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.BAD_REQUEST.getCode());
        apiResponse.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = EmployeeNotFoundException.class)
    public ApiResponse<Object> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setData(ex.getMessage());
        apiResponse.setCode(ErrorCode.Not_Found_EMPLOYEE.getCode());
        return apiResponse;
    }
    @ExceptionHandler(value = EmployeeCodeExistsException.class)
    public ApiResponse<Object> handleEmployeeNotFound(EmployeeCodeExistsException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setData(ex.getMessage());
        apiResponse.setCode(ErrorCode.CODE_EMPLOYEE_EXISTED.getCode());
        return apiResponse;
    }

    @ExceptionHandler(value = CertificateNotValidException.class)
    public ApiResponse<Object> handleEmployeeNotFound(CertificateNotValidException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setData(ex.getMessage());
        apiResponse.setCode(ErrorCode.CODE_EMPLOYEE_EXISTED.getCode());
        return apiResponse;
    }

    @ExceptionHandler(value = CommuneNotFoundException.class)
    public ApiResponse<Object> handleEmployeeNotFound(CommuneNotFoundException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setData(ex.getMessage());
        apiResponse.setCode(ErrorCode.NOT_FOUND_COMMUNE.getCode());
        return apiResponse;
    }

    @ExceptionHandler(value = DistrictNotFoundException.class)
    public ApiResponse<Object> handleEmployeeNotFound(DistrictNotFoundException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setData(ex.getMessage());
        apiResponse.setCode(ErrorCode.NOT_FOUND_DISTRICT.getCode());
        return apiResponse;
    }


    @ExceptionHandler(value = ProvinceNotFoundException.class)
    public ApiResponse<Object> handleEmployeeNotFound(ProvinceNotFoundException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setData(ex.getMessage());
        apiResponse.setCode(ErrorCode.NOT_FOUND_PROVINCE.getCode());
        return apiResponse;
    }

    @ExceptionHandler(value = DuplicateEntryException.class)
    public ApiResponse<Object> handleEmployeeNotFound(DuplicateEntryException ex) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setData(ex.getMessage());
        apiResponse.setCode(ErrorCode.BAD_REQUEST.getCode());
        return apiResponse;
    }


}