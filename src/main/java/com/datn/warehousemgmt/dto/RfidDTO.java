package com.datn.warehousemgmt.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RfidDTO {
    String rfidCode;
    Long batchId;
    String skuCode;
    LocalDate manufacturerDate;
    LocalDate expirationDate;
    Integer quantity;
    Integer action;
}
