package com.datn.warehousemgmt.service;

import com.datn.warehousemgmt.dto.IntrospectDTO;
import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    public ServiceResponse login(UserDTO dto);

    public ServiceResponse logout(UserDTO dto);

    public ServiceResponse changePassword();

    public ServiceResponse introspect(IntrospectDTO dto);
}


