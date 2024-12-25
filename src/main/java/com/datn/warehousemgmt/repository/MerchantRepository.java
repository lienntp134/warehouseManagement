package com.datn.warehousemgmt.repository;

import com.datn.warehousemgmt.entities.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {


}
