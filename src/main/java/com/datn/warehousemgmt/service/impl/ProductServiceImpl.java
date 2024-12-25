package com.datn.warehousemgmt.service.impl;

import com.datn.warehousemgmt.dto.ImportDetail;
import com.datn.warehousemgmt.dto.ProductDTO;
import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.request.ProductRequest;
import com.datn.warehousemgmt.dto.request.UserSearchRequest;
import com.datn.warehousemgmt.dto.response.ResponseImportModel;
import com.datn.warehousemgmt.entities.Category;
import com.datn.warehousemgmt.entities.Product;
import com.datn.warehousemgmt.exception.AppException;
import com.datn.warehousemgmt.exception.ErrorCode;
import com.datn.warehousemgmt.mapper.ProductMapper;
import com.datn.warehousemgmt.repository.CategoryRepository;
import com.datn.warehousemgmt.repository.ProductRepository;
import com.datn.warehousemgmt.service.ProductService;
import com.datn.warehousemgmt.utils.ImportProduct;
import com.datn.warehousemgmt.utils.PageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Value("${product.img.default}")
    private String defaultImage;
    public static final String PREFIX = "NTPL-";
    public static final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    private final ImportProduct importProduct;
    @Override
    public String generateSKUCode(){
        SecurityContextHolder.getContext().getAuthentication();
        StringBuilder code = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 8; i++) {
            code.append(alphabet.charAt(r.nextInt(alphabet.length())));
            if(i==4){
                code.append("-");
            }
        }

        if(productRepository.existsBySkuCode(code.toString())) {
            generateSKUCode();
        }
        return PREFIX + code.toString();
    }

    @Override
    public ServiceResponse getOne(String skuCode) {
        Optional<Product> optionalProduct = productRepository.findProductBySkuCode(skuCode);
        if(optionalProduct.isPresent()) {
            return new ServiceResponse(productMapper.productToProductDTO(optionalProduct.get()), "Lấy sản phẩm thành công", 200);
        }
        throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
    }

    @Override
    public ServiceResponse createProduct(ProductRequest request) {
        Product p = new Product();
        BeanUtils.copyProperties(request, p);
        if(p.getImageLink().isEmpty()){
            p.setImageLink(defaultImage);
        }
        p.getCategories().addAll(categoryRepository.findAllByIdIn(request.getCategoryIds()));
        try{
            productRepository.save(p);
            return new ServiceResponse(productMapper.productToProductDTO(p), "Tạo sản phẩm thành công", 200);
        }catch(Exception e){
            throw new AppException(ErrorCode.CREATION_FAILED);
        }
    }

    @Override
    public ServiceResponse updateProduct(ProductRequest request) {
        Optional<Product> optionalProduct = productRepository.findProductBySkuCode(request.getSkuCode());
        if(!optionalProduct.isPresent()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        Product p = optionalProduct.get();
        p.setName(request.getName());
        p.setDescription(request.getDescription());
        p.setImageLink(request.getImageLink());
        p.getCategories().addAll(categoryRepository.findAllByIdIn(request.getCategoryIds()));
        try{
            productRepository.save(p);
            return new ServiceResponse(p, "Cập nhật sản phẩm thành công", 200);
        }catch(Exception e){
            throw new AppException(ErrorCode.UPDATE_FAILED);
        }
    }

    @Override
    public ServiceResponse deleteProduct(ProductRequest request) {
        Optional<Product> optionalProduct = productRepository.findProductBySkuCode(request.getSkuCode());
        if(!optionalProduct.isPresent()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        try{
            productRepository.delete(optionalProduct.get());
            return new ServiceResponse("Xóa sản phẩm thành công", 200);
        }catch(Exception e){
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public Boolean existProductBySKUCode(String SKUCode) {
        return productRepository.existsBySkuCode(SKUCode);
    }

    @Override
    @Transactional
    public ServiceResponse importProduct(String fileName) {
        String errorPath = null;
        try {
            ImportDetail importDetail = importProduct.readFileImportUserToCourse(fileName);
            ResponseImportModel responseImportModel = new ResponseImportModel();
            responseImportModel.setTotalSuccess(importDetail.getProducts().size());
            responseImportModel.setTotalFail(importDetail.getTotalRow() - importDetail.getProducts().size());
            importProduct.importProduct(importDetail);
            if(importDetail.getError() != null || !importDetail.getError().isEmpty()) {
                errorPath = importProduct.fileErrorName(importDetail, fileName);
            }
            responseImportModel.setPathExcel(errorPath);
            return new ServiceResponse(responseImportModel, "Import thành công", 200);
        }catch (AppException e){
            throw new AppException(e.getErrorCode());
        }
    }

    @Override
    public ServiceResponse getProduct(ProductRequest request) {
        ServiceResponse response = new ServiceResponse("Không tìm thấy sản phẩm", 200);
        try{
            Pageable pageable = PageUtils.customPage(request.getPageDTO());
            Page<Product> page = productRepository.getProduct(request.getSearch(), pageable);
            if(!page.getContent().isEmpty()){
                List<ProductDTO> dto = new ArrayList<>();
                page.getContent().forEach(product -> {
                    ProductDTO productDTO = productMapper.productToProductDTO(product);
                    productDTO.setCategoryList(product.getCategories().stream().map(Category::getName).collect(Collectors.toList()));
                    dto.add(productDTO);
                });
                response.setData(dto);
                response.setMessage("Thành công");
                response.setTotalPage(page.getTotalPages());
                response.setTotalItem(page.getTotalElements());
                response.setCurrentPage(page.getNumber() + 1);
            }
            return response;
        }catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

}
