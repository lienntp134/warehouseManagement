package com.datn.warehousemgmt.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MerchantDTO {
    Long id;
    String name;
    String address;
    String phone;
    String type;
}
