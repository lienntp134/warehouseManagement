package com.datn.warehousemgmt.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportProcessResponse {
    Long batchId;
    String skuCode;
    String warehouseName;
    Integer quantity;
    LocalDate manufacturerDate;
    LocalDate expirationDate;
    LocalDate importDate;
}
