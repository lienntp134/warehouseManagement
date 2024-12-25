package com.datn.warehousemgmt.dto.request;

import com.datn.warehousemgmt.utils.PageDTO;
import lombok.Data;

@Data
public class UserSearchRequest {
    private String search;
    private Long warehouseId;
    private PageDTO pageDTO;
}
