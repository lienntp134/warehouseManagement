package com.datn.warehousemgmt.dto.request;

import com.datn.warehousemgmt.utils.PageDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductLogSearchRequest {
    private String action;
    private String search;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String status;
    private PageDTO pageDTO;
}
