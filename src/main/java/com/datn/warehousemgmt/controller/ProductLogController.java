package com.datn.warehousemgmt.controller;

import com.datn.warehousemgmt.dto.ProductLogDTO;
import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.request.ExportLogRequest;
import com.datn.warehousemgmt.dto.request.ProductLogSearchRequest;
import com.datn.warehousemgmt.service.ProductLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productLog")
@RequiredArgsConstructor
@Tag(name = "API log nhập/xuất kho")
public class ProductLogController {
    private final ProductLogService productLogService;

    @Operation(summary = "Thay đổi trạng thái log")
    @PutMapping("/change-status")
    public ResponseEntity<?> changeLogStatus(@RequestBody ProductLogDTO dto){
        ServiceResponse result = productLogService.changeStatus(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Tìm kiếm, lấy danh sách log")
    @PostMapping("/find-log")
    public ResponseEntity<?> findLog(@RequestBody ProductLogSearchRequest request){
        return new ResponseEntity<>(productLogService.findLog(request), HttpStatus.OK);
    }

    @Operation(summary = "Chi tiết log")
    @GetMapping("/{id}")
    public ResponseEntity<?> getLog(@PathVariable("id") Long id){
        return new ResponseEntity<>(productLogService.getLogDetail(id), HttpStatus.OK);
    }

    @Operation(summary = "Xuất báo cáo")
    @GetMapping("/export")
    public ResponseEntity<?> exportLog(HttpServletResponse response,
                                       @RequestBody ExportLogRequest request){
        return new ResponseEntity<>(productLogService.exportLog(response, request), HttpStatus.OK);
    }
}
