package com.datn.warehousemgmt.controller;

import com.datn.warehousemgmt.dto.ProductDTO;
import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.request.ImportRequest;
import com.datn.warehousemgmt.dto.request.ProductRequest;
import com.datn.warehousemgmt.exception.AppException;
import com.datn.warehousemgmt.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Tag(name = "API sản phẩm")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "API sinh mã sản phẩm")
    @GetMapping("/genId")
    public ResponseEntity<?> generateId(){
        return new ResponseEntity<>(productService.generateSKUCode(), HttpStatus.OK);
    }

    @Operation(summary = "Lấy thông tin sản phẩm theo SKU code")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") String skuCode){
        return new ResponseEntity<>(productService.getOne(skuCode), HttpStatus.OK);
    }

    @Operation(summary = "Tạo sản phẩm mới")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest dto){
        return new ResponseEntity<>(productService.createProduct(dto), HttpStatus.OK);
    }

    @Operation(summary = "Cập nhật thông tin sản phẩm")
    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequest dto){
        return new ResponseEntity<>(productService.updateProduct(dto), HttpStatus.OK);
    }

    @Operation(summary = "Xóa sản phẩm")
    @DeleteMapping
    public ResponseEntity<?> deleteProduct(@RequestBody ProductRequest dto){
        return new ResponseEntity<>(productService.deleteProduct(dto), HttpStatus.OK);
    }

    @Operation(summary = "Import sản phẩm từ file csv")
    @PostMapping("/import")
    public ResponseEntity<?> importProduct(@RequestBody ImportRequest request){
        return new ResponseEntity<>(productService.importProduct(request.getFilename()),
                HttpStatus.OK);
    }

    @Operation(summary = "Lấy danh sách sản phẩm")
    @PostMapping("/get")
    public ResponseEntity<?> getProduct(@RequestBody ProductRequest request){
        return new ResponseEntity<>(productService.getProduct(request),
                HttpStatus.OK);
    }
}
