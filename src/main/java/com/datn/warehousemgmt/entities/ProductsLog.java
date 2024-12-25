package com.datn.warehousemgmt.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product_log")
@EntityListeners(AuditingEntityListener.class)
public class ProductsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "batch_product_id", referencedColumnName = "id")
    BatchProduct batchProduct;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "action")
    String action;

    @CreatedDate
    LocalDateTime createdDate;

    @CreatedBy
    String createdBy;

    @Column(name = "status")
    String status;

    @ManyToOne
    @JoinColumn(name = "merchant_id", referencedColumnName = "id")
    Merchant merchant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "log_details",
            joinColumns = @JoinColumn(name = "product_log_id"),
            inverseJoinColumns = @JoinColumn(name = "packet_id"))
    private List<Packet> packets = new ArrayList<>();

}