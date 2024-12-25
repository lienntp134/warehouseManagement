package com.datn.warehousemgmt.dto.request;

import com.datn.warehousemgmt.utils.PageDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    String skuCode;
    String name;
    String description;
    String imageLink;
    List<Long> categoryIds;
    PageDTO pageDTO;
    String search;
}
