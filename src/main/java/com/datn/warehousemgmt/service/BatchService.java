package com.datn.warehousemgmt.service;

import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.request.BatchRequest;
import com.datn.warehousemgmt.entities.BatchProduct;

public interface BatchService {
    ServiceResponse createBatch();
    ServiceResponse updateBatch(BatchRequest batchRequest);
    BatchProduct getBatchById(Long id);

    BatchProduct save(BatchProduct batchProduct);

    ServiceResponse getBatchBySku(BatchRequest request);
}
