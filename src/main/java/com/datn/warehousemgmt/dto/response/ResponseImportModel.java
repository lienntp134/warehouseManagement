package com.datn.warehousemgmt.dto.response;

import lombok.*;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseImportModel {
    private Integer totalSuccess;
    private Integer totalFail;
    private String pathExcel;
}
