package com.datn.warehousemgmt.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PacketDTO {
    Long id;
    String rfidTag;
    Integer quantity;
    Long batchId;
    String status;
}
