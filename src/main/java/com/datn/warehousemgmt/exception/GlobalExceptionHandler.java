package com.datn.warehousemgmt.exception;


import com.datn.warehousemgmt.dto.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ServiceResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ServiceResponse serviceResponse = new ServiceResponse();

        serviceResponse.setCode(errorCode.getCode());
        serviceResponse.setMessage(errorCode.getMessage());
        serviceResponse.setData(errorCode.getStatusCode());

        return ResponseEntity.status(errorCode.getStatusCode()).body(serviceResponse);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ServiceResponse> handlingException(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ServiceResponse(exception.getMessage(), 400));
    }
}
