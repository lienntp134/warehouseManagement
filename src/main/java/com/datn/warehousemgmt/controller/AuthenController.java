package com.datn.warehousemgmt.controller;

import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.UserDTO;
import com.datn.warehousemgmt.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.OutputKeys;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
@Tag(name = "API Authentication")
public class AuthenController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Đăng nhập")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO dto){
        try{
            return new ResponseEntity<>(authenticationService.login(dto), HttpStatus.OK);
        }catch(Exception e){
            ServiceResponse response = new ServiceResponse(e.getMessage(), 401);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
