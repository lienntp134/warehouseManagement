package com.datn.warehousemgmt.service;

import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.WareHouseDTO;
import com.datn.warehousemgmt.entities.WareHouse;

public interface WareHouseService {
    WareHouse getById(Long warehouseId);
    ServiceResponse addNewWareHouse(WareHouseDTO dto);
    ServiceResponse updateWareHouse(WareHouseDTO dto);
    ServiceResponse deleteWareHouse(WareHouseDTO dto);

}
