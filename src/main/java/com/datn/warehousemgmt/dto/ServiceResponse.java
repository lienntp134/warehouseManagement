package com.datn.warehousemgmt.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceResponse {
    String message;
    Integer code;
    Object data;
    Integer totalPage;
    Long totalItem;
    Integer currentPage;

    public ServiceResponse(String message, Integer code){
        this.message = message;
        this.code = code;
    }

    public ServiceResponse(Object data, String message, Integer code){
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public ServiceResponse(Object data, String message, Integer code, Integer totalPage, Long totalItem, Integer currentPage){
        this.message = message;
        this.code = code;
        this.data = data;
        this.totalPage = totalPage;
        this.totalItem = totalItem;
        this.currentPage = currentPage;
    }
}
