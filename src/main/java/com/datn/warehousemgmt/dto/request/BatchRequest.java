package com.datn.warehousemgmt.dto.request;

import com.datn.warehousemgmt.utils.PageDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchRequest {
    Long id;
    String productSkuCode;
    Integer quantity;
    LocalDate manufacturerDate;
    LocalDate expirationDate;
    Long warehouseId;
    PageDTO pageDTO;
    Long supplierId;
    String status;
    BigInteger inPrice;
    BigInteger outPrice;
}
