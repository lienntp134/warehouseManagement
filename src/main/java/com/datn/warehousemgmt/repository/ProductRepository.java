package com.datn.warehousemgmt.repository;

import com.datn.warehousemgmt.dto.request.UserSearchRequest;
import com.datn.warehousemgmt.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p FROM Product p " +
            "WHERE (:search IS NULL " +
            "OR :search = '' " +
            "OR LOWER(p.name) LIKE concat('%', coalesce(:search, ''), '%') " +
            "OR LOWER(p.skuCode) LIKE concat('%', coalesce(:search, ''), '%'))")
    Page<Product> getProduct(@Param("search") String search, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.skuCode =  ?1")
    Optional<Product> findProductBySkuCode(String skuCode);

    @Query(value = "SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Product p WHERE p.skuCode = ?1")
    Boolean existsBySkuCode(String skuCode);
}
