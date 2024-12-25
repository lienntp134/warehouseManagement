package com.datn.warehousemgmt.service;

import com.datn.warehousemgmt.dto.MerchantDTO;
import com.datn.warehousemgmt.entities.Merchant;

public interface MerchantService {
    Merchant createMerchant(MerchantDTO dto);
}
