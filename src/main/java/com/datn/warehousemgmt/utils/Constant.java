package com.datn.warehousemgmt.utils;

import lombok.Getter;

public class Constant {
    public static final String ADMIN_ROLE = "ADMIN";

    public enum ProductLogStatus {
        PENDING,
        CLOSED;
    }

    @Getter
    public enum ProductLogAction {
        IMPORT(1),
        EXPORT(0);

        private final Integer action;

        ProductLogAction(Integer action) {
            this.action = action;
        }
    }

    @Getter
    public enum PacketStatus{
        IN_STOCK("Đã nhập kho"),
        EXPORTED("Đã xuất kho");

        private final String status;

        PacketStatus(String status) {
            this.status = status;
        }

    }

    public enum MerchantType {
        SUPPLIER,
        BUYER;
    }
}
