package com.datn.warehousemgmt.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.exception.AppException;
import com.datn.warehousemgmt.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public ServiceResponse uploadFile(MultipartFile file) throws IOException {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.emptyMap());
            return new ServiceResponse(uploadResult.get("url"), "Lưu ảnh thành công", 200);
        }catch (IOException e) {
            throw new AppException(ErrorCode.IO_EXCEPTION);
        }
    }
}