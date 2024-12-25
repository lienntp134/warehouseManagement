package com.datn.warehousemgmt.controller;

import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.UserDTO;
import com.datn.warehousemgmt.dto.request.UserSearchRequest;
import com.datn.warehousemgmt.service.UserService;
import com.datn.warehousemgmt.utils.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "API quản lý người dùng")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Tạo user mới")
    @PostMapping()
    public ResponseEntity<ServiceResponse> createUser(@RequestBody UserDTO dto){
        return ResponseEntity.ok(userService.create(dto));
    }

    @Operation(summary = "Cập nhật thông tin user")
    @PutMapping("/{uId}")
    public ResponseEntity<ServiceResponse> updateUser(@PathVariable Long uId, @RequestBody UserDTO dto){
        return ResponseEntity.ok(userService.update(uId, dto));
    }

    @Operation(summary = "Xóa thông tin user")
    @DeleteMapping("/{uId}")
    public ResponseEntity<ServiceResponse> deleteUser(@PathVariable Long uId){
        userService.delete(uId);
        return ResponseEntity.ok(new ServiceResponse("Xóa user thành công", 200));
    }

    @Operation(summary = "Thông tin user")
    @GetMapping("/{uId}")
    public ResponseEntity<?> getUserById(@PathVariable("uId") Long uId){
        return new ResponseEntity<>(userService.findById(uId), HttpStatus.OK);
    }

    @Operation(summary = "Lấy danh sách/Tìm kiếm thông tin user")
    @PostMapping("/get-more")
    public ResponseEntity<ServiceResponse> getAllUsers(@RequestBody UserSearchRequest request){
        return ResponseEntity.ok(userService.getAll(request));
    }
}
