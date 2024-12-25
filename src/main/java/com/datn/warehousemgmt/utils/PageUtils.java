package com.datn.warehousemgmt.utils;

import com.datn.warehousemgmt.dto.request.UserSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtils {

    public static Pageable customPage(PageDTO request){
        if(request == null){
            return PageRequest.of(0, 10,
                    Sort.Direction.fromString("asc"), "id");
        }
        if(request.getPage() == null){
            request.setPage(0);
        }
        if(request.getSize() == null){
            request.setSize(10);
        }
        if(StringUtils.isBlank(request.getSortBy())){
            request.setSortBy("id");
        }
        if(StringUtils.isBlank(request.getSortType())){
            request.setSortType("asc");
        }

        return PageRequest.of(request.getPage(), request.getSize(),
                Sort.Direction.fromString(request.getSortType()), request.getSortBy());
    }
}