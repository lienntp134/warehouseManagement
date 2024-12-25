package com.datn.warehousemgmt.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(0, "Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_EXISTED(0, "Tài khoản đã tồn tại", HttpStatus.BAD_REQUEST),

    USER_NOT_EXISTED(0, "Tài khoản không tồn tại", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(0, "Yêu cầu xác thực tài khoản", HttpStatus.UNAUTHORIZED),

    INVALID_ACCOUNT(0, "Sai tài khoản hoặc mật khẩu", HttpStatus.BAD_REQUEST),

    CREATION_FAILED(0, "Khởi tạo không thành công", HttpStatus.BAD_REQUEST),
    UPDATE_FAILED(0, "Cập nhật không thành công", HttpStatus.BAD_REQUEST),
    DELETE_FAILED(0, "Xóa không thành công", HttpStatus.BAD_REQUEST),
    IMPORT_FAILED(0, "Import không thành công", HttpStatus.BAD_REQUEST),

    PRODUCT_NOT_FOUND(0, "Không tìm thấy sản phẩm", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(0, "Không tìm thấy danh mục", HttpStatus.NOT_FOUND),
    BATCH_NOT_FOUND(0, "Không tìm thấy thông tin lô", HttpStatus.NOT_FOUND),
    BATCH_INFORMATION_NOT_MATCH(0, "Thông tin lô không khớp trong cơ sở dữ liệu", HttpStatus.NOT_FOUND),

    FILE_NOT_FOUND(0, "Không tìm thấy file", HttpStatus.NOT_FOUND),
    IO_EXCEPTION(0, "Xảy ra lỗi trong quá trình đọc file", HttpStatus.NOT_FOUND),
    SESSION_EXPIRED(0, "Session timeout", HttpStatus.BAD_REQUEST),

    NAME_NOT_NULL(0, "Tên không được để trống", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_NULL(0, "Địa chỉ không được để trống", HttpStatus.BAD_REQUEST),
    WRONG_INPUT(0, "Vui lòng nhập đủ các trưng thông tin", HttpStatus.BAD_REQUEST),

    FILE_NOT_ALLOWED(0, "Định dạng file không được cho phép", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    FILE_FALSE_FORMAT(0, "Định dạng file không đúng theo template", HttpStatus.BAD_REQUEST),
    WRONG_INPUT1(0, "Vui lòng nhập đủ các trưng thông tin", HttpStatus.BAD_REQUEST),
    WRONG_INPUT2(0, "Vui lòng nhập đủ các trưng thông tin", HttpStatus.BAD_REQUEST),

    IMPORT_GOODS_LIST_EMPTY(0, "Không có nhận diện được hàng hóa nhập", HttpStatus.BAD_REQUEST),
    INVALID_RFID_TAG(0, "Thẻ không hợp lệ", HttpStatus.BAD_REQUEST),
    PACKET_ALREADY_IMPORTED(0, "Packet đã tồn tại trong kho", HttpStatus.BAD_REQUEST),
    PACKET_ALREADY_EXPORTED(0, "Packet đã xuất kho", HttpStatus.BAD_REQUEST),
    PACKET_NOT_FOUND(0, "Packet không tồn tại", HttpStatus.BAD_REQUEST),

    IMPORT_TICKET_NOT_FOUND(0, "Không tìm thấy phiếu nhập", HttpStatus.BAD_REQUEST),

    WARE_HOUSE_NOT_EXIST(0, "Không tìm thấy thông tin kho hàng", HttpStatus.BAD_REQUEST),

    MERCHANT_NOT_FOUND(0, "Không tìm thấy thông tin đối tác", HttpStatus.BAD_REQUEST),

    ACTION_NOT_DEFINE(0, "Không xác định được hành động", HttpStatus.BAD_REQUEST),
    ;


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

}
