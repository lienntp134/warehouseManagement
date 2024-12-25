package com.datn.warehousemgmt.dto;

import com.datn.warehousemgmt.entities.BatchProduct;
import com.datn.warehousemgmt.entities.Packet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductLogDTO {
    Long id;
    Long batchProductId;
    String supplierName;
    Integer quantity;
    String action;
    String status;
    Long merchantId;
    List<Packet> packets = new ArrayList<>();
}
