package com.datn.warehousemgmt.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @Column(name = "sku_code")
    String skuCode;

    @Column(name = "name", columnDefinition = "nvarchar(max)")
    String name;

    @Column(name = "description", columnDefinition = "nvarchar(max)")
    String description;

    @Column(name = "image_link", columnDefinition = "nvarchar(max)")
    String imageLink;

    @CreatedDate
    LocalDateTime createdDate;

    @CreatedBy
    String username;

    @ManyToMany
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_sku_code", referencedColumnName = "sku_code"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_sku_code", referencedColumnName = "sku_code")
    private List<BatchProduct> batchProducts = new ArrayList<>();

}
