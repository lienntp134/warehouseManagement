package com.datn.warehousemgmt.service.impl;

import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.WareHouseDTO;
import com.datn.warehousemgmt.entities.WareHouse;
import com.datn.warehousemgmt.exception.AppException;
import com.datn.warehousemgmt.exception.ErrorCode;
import com.datn.warehousemgmt.repository.WareHouseRepository;
import com.datn.warehousemgmt.service.WareHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WareHouseServiceImpl implements WareHouseService {
    private final WareHouseRepository wareHouseRepository;

    @Override
    public ServiceResponse addNewWareHouse(WareHouseDTO dto) {
        return null;
    }

    @Override
    public ServiceResponse updateWareHouse(WareHouseDTO dto) {
        return null;
    }

    @Override
    public ServiceResponse deleteWareHouse(WareHouseDTO dto) {
        return null;
    }

    @Override
    public WareHouse getById(Long warehouseId) {
        return wareHouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(ErrorCode.WARE_HOUSE_NOT_EXIST));
    }

    public void validateDto(WareHouseDTO dto) {
        if(dto.getName() == null || dto.getName().isEmpty()) {

        }
    }
}
