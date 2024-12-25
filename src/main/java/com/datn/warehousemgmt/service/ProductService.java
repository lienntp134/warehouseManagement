package com.datn.warehousemgmt.service;

import com.datn.warehousemgmt.dto.ProductDTO;
import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.request.ProductRequest;

public interface ProductService {

    String generateSKUCode();

    ServiceResponse getOne(String skuCode);
    ServiceResponse createProduct(ProductRequest request);
    ServiceResponse updateProduct(ProductRequest request);
    ServiceResponse deleteProduct(ProductRequest request);
    ServiceResponse importProduct(String fileName);

    ServiceResponse getProduct(ProductRequest request);

    Boolean existProductBySKUCode(String SKUCode);
}
