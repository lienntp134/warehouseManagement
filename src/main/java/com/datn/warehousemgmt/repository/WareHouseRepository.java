package com.datn.warehousemgmt.repository;

import com.datn.warehousemgmt.entities.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouse, Long> {

}
