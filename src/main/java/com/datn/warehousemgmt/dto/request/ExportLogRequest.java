package com.datn.warehousemgmt.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportLogRequest {
    String skuCode;
    Long batchId;
    String action;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
