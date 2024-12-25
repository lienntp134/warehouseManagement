package com.datn.warehousemgmt.controller;

import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.request.BatchRequest;
import com.datn.warehousemgmt.service.BatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
@Tag(name = "API Lô hàng")
public class BatchController {

    private final BatchService batchService;

    @Operation(summary = "Lấy thông tin lô hàng theo mã")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getBatchById(@PathVariable Long id){
        ServiceResponse result = new ServiceResponse(batchService.getBatchById(id), "Lấy thông tin thành công", 200);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Cập nhật thông tin lô hàng")
    @PutMapping()
    public ResponseEntity<ServiceResponse> update(@RequestBody BatchRequest request){
        return new ResponseEntity<>(batchService.updateBatch(request), HttpStatus.OK);
    }

    @Operation(summary = "Tìm kiếm thông tin lô hàng")
    @PostMapping("/findBatch")
    public ResponseEntity<?> findBatch(@RequestBody BatchRequest request){
        return new ResponseEntity<>(batchService.getBatchBySku(request), HttpStatus.OK);
    }
}
