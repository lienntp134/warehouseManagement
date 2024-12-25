package com.datn.warehousemgmt.exception;

import lombok.Getter;
import org.springframework.dao.EmptyResultDataAccessException;

@Getter
public class AppException extends RuntimeException {
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;
}
