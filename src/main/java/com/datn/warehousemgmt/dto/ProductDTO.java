package com.datn.warehousemgmt.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    String skuCode;
    String name;
    String description;
    String imageLink;
    List<String> categoryList = new ArrayList<>();
}
