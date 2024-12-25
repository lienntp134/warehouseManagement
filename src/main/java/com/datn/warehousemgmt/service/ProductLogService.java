package com.datn.warehousemgmt.service;

import com.datn.warehousemgmt.dto.ProductLogDTO;
import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.request.ExportLogRequest;
import com.datn.warehousemgmt.dto.request.ProductLogSearchRequest;
import com.datn.warehousemgmt.entities.ProductsLog;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface ProductLogService {

    ProductsLog createLog(ProductLogDTO request);

    ProductsLog updateLog(ProductLogDTO request);

    ServiceResponse changeStatus(ProductLogDTO request);

    Optional<ProductsLog> findByStatusActionAndBatchId(String status, String action, Long batchId);

    ServiceResponse findLog(ProductLogSearchRequest request);

    ServiceResponse getLogDetail(Long id);

    ServiceResponse exportLog(HttpServletResponse response, ExportLogRequest request);
}