package com.datn.warehousemgmt.repository;

import com.datn.warehousemgmt.dto.request.UserSearchRequest;
import com.datn.warehousemgmt.entities.BatchProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BatchProductRepository extends JpaRepository<BatchProduct, Long> {

    @Query("SELECT bp FROM BatchProduct bp " +
            "WHERE (?1 IS NULL OR ?1 = '' OR bp.productSkuCode = ?1) " +
            "AND (?2 IS NULL OR bp.supplierId = ?2) " +
            "AND (?3 IS NULL OR ?3 = '' OR " +
            "(?3 LIKE 'expired' AND bp.expirationDate < CURRENT_TIMESTAMP) " +
            "OR (?3 LIKE 'noteExpired' AND bp.expirationDate >= CURRENT_TIMESTAMP))")
    Page<BatchProduct> getAllBySkuCode(String skuCode, Long supplierId,
                                       String status, Pageable pageable);
}
