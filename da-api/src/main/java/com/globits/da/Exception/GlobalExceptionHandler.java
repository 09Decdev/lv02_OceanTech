package com.globits.da.Exception;

import com.globits.da.dto.response.ApiResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Component
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE="min";
    private static final String MAX_ATTRIBUTE="max";
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleExceptions(Exception ex) {
//        FieldError error = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
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
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("IllegalArgumentException: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(4001);
        response.setMessage(ex.getMessage());
        response.setData(null);

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(value = ConstraintViolationException.class)
    ResponseEntity handleConstraintViolationException(ConstraintViolationException ex){
        log.error("ConstraintViolationException: {}", ex.getMessage());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
        apiResponse.setMessage(ErrorCode.INVALID_KEY.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class )
    ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        log.error("DataIntegrityViolationException: {}", ex.getMessage());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
        apiResponse.setMessage(ErrorCode.INVALID_KEY.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class )
    ResponseEntity handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex){
        log.error("SQLIntegrityConstraintViolationException: {}", ex.getMessage());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
        apiResponse.setMessage(ErrorCode.INVALID_KEY.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError error = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String errorMessage = (error != null) ? error.getDefaultMessage() : "Validation error";

        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.BAD_REQUEST.getCode());
        apiResponse.setMessage(errorMessage);
        apiResponse.setData(null);
        return ResponseEntity.badRequest().body(apiResponse);
    }



    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }


}