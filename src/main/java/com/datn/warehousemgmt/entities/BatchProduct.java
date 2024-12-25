package com.datn.warehousemgmt.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "batch")
public class BatchProduct {

    @Id
    Long id;

    @Column(name = "product_sku_code")
    String productSkuCode;

    @Column(name = "supplier_id")
    Long supplierId;

    @Column(name = "ware_house_id")
    Long warehouseId;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "manufacture_date")
    LocalDate manufacturerDate;

    @Column(name = "expiration_date")
    LocalDate expirationDate;

    @Column(name = "in_price")
    BigInteger inPrice;

    @Column(name = "out_price")
    BigInteger outPrice;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "batch_id", referencedColumnName = "id")
    private List<Packet> packets = new ArrayList<>();
}
