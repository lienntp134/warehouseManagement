package com.datn.warehousemgmt.service.impl;

import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.UserDTO;
import com.datn.warehousemgmt.dto.request.UserSearchRequest;
import com.datn.warehousemgmt.entities.Users;
import com.datn.warehousemgmt.exception.AppException;
import com.datn.warehousemgmt.exception.ErrorCode;
import com.datn.warehousemgmt.repository.UsersRepository;
import com.datn.warehousemgmt.service.UserService;
import com.datn.warehousemgmt.utils.PageDTO;
import com.datn.warehousemgmt.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    @Override
    public ServiceResponse create(UserDTO dto) {
        ServiceResponse res = new ServiceResponse("Tạo user thành công", 200);
        try{
            Users user = new Users();
            BeanUtils.copyProperties(dto, user);
            res.setData(usersRepository.save(user));
            return res;
        }catch (Exception ex){
            return new ServiceResponse("Tạo user thất bại: " + ex.getMessage(), 400);
        }
    }

    @Override
    public ServiceResponse update(Long userId, UserDTO dto) {
        ServiceResponse res = new ServiceResponse("Chỉnh sửa thông tin thành công", 200);
        try{
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));
            user.setName(dto.getName());
            res.setData(usersRepository.save(user));
            return res;
        }catch (Exception ex){
            return new ServiceResponse("Chỉnh sửa thông tin user thất bại: " + ex.getMessage(), 400);
        }
    }

    @Override
    public ServiceResponse delete(Long uId) {
        ServiceResponse res = new ServiceResponse("Xóa thành công", 200);
        try{
            Users user = usersRepository.findById(uId)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));
            usersRepository.delete(user);
            return res;
        }catch (Exception ex){
            return new ServiceResponse("Xóa thất bại: " + ex.getMessage(), 400);
        }
    }

    @Override
    public ServiceResponse findById(Long userId) {
        return new ServiceResponse(usersRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)),
                "Lấy thông tin user thành công", 200);
    }

    @Override
    public ServiceResponse getAll(UserSearchRequest request) {
        Pageable pageable = PageUtils.customPage(request.getPageDTO());
        Page<Users> users = usersRepository.getAllUser(request.getSearch(),
                request.getWarehouseId(), pageable);
        return new ServiceResponse(users, "Lấy danh sách user thành công", 200,
                users.getTotalPages(), users.getTotalElements(), users.getNumber() + 1);
    }
}
