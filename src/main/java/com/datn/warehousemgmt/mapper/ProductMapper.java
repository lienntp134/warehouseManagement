package com.datn.warehousemgmt.mapper;

import com.datn.warehousemgmt.dto.ProductDTO;
import com.datn.warehousemgmt.dto.ProductLogDTO;
import com.datn.warehousemgmt.dto.response.ProductLogListResponse;
import com.datn.warehousemgmt.entities.Category;
import com.datn.warehousemgmt.entities.Packet;
import com.datn.warehousemgmt.entities.Product;
import com.datn.warehousemgmt.entities.ProductsLog;
import com.datn.warehousemgmt.utils.DateTimeUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface ProductMapper {

//    @Mapping(source = "categories", target = "categoryList",
//            qualifiedByName = "mapCategoryNames")
    ProductDTO productToProductDTO(Product product);

    Product productDTOToProduct(ProductDTO productDTO);

    @Named("mapCategoryNames")
    default List<String> mapCategoryNames(List<Category> categories) {
        if (categories == null) {
            return new ArrayList<>();
        }
        return categories.stream()
                .map(Category::getName)
                .toList();
    }

    default ProductLogDTO fromEntityToLogDTO(ProductsLog productsLog){
        ProductLogDTO productLogDTO = new ProductLogDTO();
        productLogDTO.setId(productsLog.getId());
        productLogDTO.setBatchProductId(productsLog.getBatchProduct().getId());
        productLogDTO.setQuantity(productsLog.getQuantity());
        productLogDTO.setAction(productsLog.getAction());
        productLogDTO.setStatus(productsLog.getStatus());
        productLogDTO.setMerchantId(productsLog.getMerchant() == null ? null : productsLog.getMerchant().getId());
        productLogDTO.setPackets(new ArrayList<>(productsLog.getPackets()));
        return productLogDTO;
    };

    default ProductLogListResponse toSearchLog(Map<String, Object> obj, List<Packet> packets){
        ProductLogListResponse response = new ProductLogListResponse();
        response.setId((Long) obj.get("id"));
        response.setSkuCode((String) obj.get("skuCode"));
        response.setProductName((String) obj.get("name"));
        response.setBatchProductId((Long) obj.get("batchId"));
        response.setWorker((String) obj.get("worker"));
        Timestamp createdDate = (Timestamp) obj.get("createdDate");
        response.setLogDate(createdDate == null ? null : DateTimeUtils.formatLocalDateTime(createdDate.toLocalDateTime()));
        response.setQuantity((Integer) obj.get("quantity"));
        response.setAction((String) obj.get("action"));
        response.setStatus((String) obj.get("status"));
        response.setPackets(packets);
        return response;
    }
}
