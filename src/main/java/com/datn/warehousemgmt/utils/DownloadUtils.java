package com.datn.warehousemgmt.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.util.FileCopyUtils;

import java.io.*;

public class DownloadUtils {
    public static void download(HttpServletResponse response, File file) throws Exception {
        if (!file.exists()){
            throw new Exception();

        }
        InputStream inputStream = null;
        try {
            String nameFile = file.getName().replaceAll("_[a-f0-9\\-]{36}(?=\\.xlsx$)", "");
            byte[] data = FileUtils.readFileToByteArray(file);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition","attachment;filename="+nameFile);
            response.setContentLength(data.length);
            inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            FileCopyUtils.copy(inputStream,response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            try {
                response.sendRedirect("/admin");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
