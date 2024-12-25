package com.datn.warehousemgmt.dto;

import com.datn.warehousemgmt.dto.request.ProductRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportDetail {
    private List<ProductRequest> products = new ArrayList<>();
    private List<List<String>> error = new ArrayList<>();
    private Integer totalRow;
}
