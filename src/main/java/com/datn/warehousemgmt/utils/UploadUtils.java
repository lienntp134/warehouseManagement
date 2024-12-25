package com.datn.warehousemgmt.utils;

import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.exception.AppException;
import com.datn.warehousemgmt.exception.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component("upload")
public class UploadUtils {

    @Value("${upload.folder}")
    private String uploadFolder;

    private final List<String> allowPrefixes = Arrays.asList("csv", "xlsx", "xls");

    public ServiceResponse saveExcelFile(MultipartFile multipartFile) {
        validateUpload(multipartFile);
        String name = StringUtils.substringBeforeLast(multipartFile.getOriginalFilename(), ".");
        String subFix = StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), ".");
        StringBuilder originName =
                new StringBuilder(uploadFolder + name + "-" + UUID.randomUUID() + "." + subFix);
        File file = new File(originName.toString());
        try {
            multipartFile.transferTo(file);
            return new ServiceResponse(file.getName(), 200);
        } catch (IOException e) {
            throw new AppException(ErrorCode.IO_EXCEPTION);
        } catch(Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public void validateUpload(MultipartFile multipartFile){
        String subFix = StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), ".");
        if(!allowPrefixes.contains(subFix)){
            throw new AppException(ErrorCode.FILE_NOT_ALLOWED);
        }
    }
}
