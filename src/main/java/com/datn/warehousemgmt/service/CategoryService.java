package com.datn.warehousemgmt.service;

import com.datn.warehousemgmt.dto.CategoryDTO;
import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.entities.Category;

public interface CategoryService {

    ServiceResponse createCategory(CategoryDTO dto);

    ServiceResponse updateCategory(CategoryDTO dto);

    ServiceResponse deleteCategory(CategoryDTO dto);
}
