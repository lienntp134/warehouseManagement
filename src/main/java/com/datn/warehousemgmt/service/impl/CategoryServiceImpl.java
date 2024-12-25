package com.datn.warehousemgmt.service.impl;

import com.datn.warehousemgmt.dto.CategoryDTO;
import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.entities.Category;
import com.datn.warehousemgmt.exception.AppException;
import com.datn.warehousemgmt.exception.ErrorCode;
import com.datn.warehousemgmt.repository.CategoryRepository;
import com.datn.warehousemgmt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public ServiceResponse createCategory(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        try{
            return new ServiceResponse(categoryRepository.save(category), "Tạo danh mục thành công", 200);
        }catch (Exception e){
            throw new AppException(ErrorCode.CREATION_FAILED);
        }
    }

    @Override
    public ServiceResponse updateCategory(CategoryDTO dto) {
        Category category = new Category();
        try{
            Optional<Category> categoryOptional = categoryRepository.findById(dto.getId());
            if(categoryOptional.isPresent()){
                category = categoryOptional.get();
                category.setName(dto.getName());
            }
            return new ServiceResponse(categoryRepository.save(category), "Cập nhật danh mục thành công", 200);
        }catch (Exception e){
            throw new AppException(ErrorCode.UPDATE_FAILED);
        }
    }

    @Override
    public ServiceResponse deleteCategory(CategoryDTO dto) {
        try{
            Optional<Category> categoryOptional = categoryRepository.findById(dto.getId());
            if(!categoryOptional.isPresent()){
                throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
            }
            categoryRepository.delete(categoryOptional.get());
            return new ServiceResponse("Xóa danh mục thành công", 200);
        }catch (Exception e){
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
    }
}
