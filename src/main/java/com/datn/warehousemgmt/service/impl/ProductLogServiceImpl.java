package com.datn.warehousemgmt.service.impl;

import com.datn.warehousemgmt.dto.ProductLogDTO;
import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.request.ExportLogRequest;
import com.datn.warehousemgmt.dto.request.ProductLogSearchRequest;
import com.datn.warehousemgmt.dto.request.UserSearchRequest;
import com.datn.warehousemgmt.dto.response.ProductLogListResponse;
import com.datn.warehousemgmt.entities.*;
import com.datn.warehousemgmt.exception.AppException;
import com.datn.warehousemgmt.exception.ErrorCode;
import com.datn.warehousemgmt.mapper.ProductMapper;
import com.datn.warehousemgmt.repository.BatchProductRepository;
import com.datn.warehousemgmt.repository.MerchantRepository;
import com.datn.warehousemgmt.repository.PacketRepository;
import com.datn.warehousemgmt.repository.ProductLogRepository;
import com.datn.warehousemgmt.service.ProductLogService;
import com.datn.warehousemgmt.utils.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductLogServiceImpl implements ProductLogService {

    private final ProductLogRepository productLogRepository;
    private final BatchProductRepository batchProductRepository;
    private final PacketRepository packetRepository;
    private final MerchantRepository merchantRepository;

    private final UserUtils utils;
    private final ProductMapper productMapper;
    private final ExcelExport excelExport;

    @Value("${excel.log.folder}")
    public String BASE_DIR;

    @Override
    public Optional<ProductsLog> findByStatusActionAndBatchId(String status, String action, Long batchId) {
        return productLogRepository.findByStatusAndActionIsAndBatchProductId(status, action, batchId);
    }
    @Override
    @Transactional
    public ProductsLog createLog(ProductLogDTO request) {
        ProductsLog productsLog = new ProductsLog();
        BatchProduct batchProduct = batchProductRepository.findById(request.getBatchProductId())
                        .orElseThrow(() -> new AppException(ErrorCode.BATCH_NOT_FOUND));
        productsLog.setBatchProduct(batchProduct);
        productsLog.setAction(request.getAction());
        productsLog.setStatus(request.getStatus());
        productsLog.setQuantity(0);
        return productLogRepository.save(productsLog);
    }

    @Override
    @Transactional
    public ProductsLog updateLog(ProductLogDTO request) {
        ProductsLog productsLog = productLogRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.IMPORT_TICKET_NOT_FOUND));
        productsLog.setQuantity(request.getQuantity());
        productsLog.setStatus(request.getStatus());
        productsLog.getPackets().clear();
        productsLog.getPackets().addAll(request.getPackets());
        return productLogRepository.save(productsLog);
    }

    @Override
    public ServiceResponse changeStatus(ProductLogDTO request) {
        try {
            ProductsLog productsLog = productLogRepository.findById(request.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.IMPORT_TICKET_NOT_FOUND));
            Merchant merchant = merchantRepository.findById(request.getMerchantId())
                    .orElseThrow(() -> new AppException(ErrorCode.MERCHANT_NOT_FOUND));
            productsLog.setStatus(request.getStatus());
            productsLog.setMerchant(merchant);
            productsLog = productLogRepository.save(productsLog);
            return new ServiceResponse("Cập nhật trạng thái thành công: " + productsLog.getStatus(), 200);
        }catch (Exception e){
            throw new AppException(ErrorCode.UPDATE_FAILED);
        }
    }

    @Override
    public ServiceResponse findLog(ProductLogSearchRequest request) {
        try {
            Users u = utils.getMyUser();
            Pageable pageable = PageUtils.customPage(request.getPageDTO());
            if(u.getPermissions().stream().map(Permission::getName)
                    .toList().contains(Constant.ADMIN_ROLE)){
                u.setWarehouseId(null);
            }
            Page<Map<String, Object> > page = productLogRepository.findLog(request.getSearch(),
                    request.getStartDateTime(),
                    request.getEndDateTime(),
                    request.getAction(),
                    request.getStatus(),
                    u.getWarehouseId(),
                    pageable);
            List<ProductLogListResponse> result = page.stream().map(res -> {
                List<Packet> packets = packetRepository.getPacketList((Long) res.get("id"));
                return productMapper.toSearchLog(res, packets);
            }).collect(Collectors.toList());

            return new ServiceResponse(result, "Lấy danh sách thành công", 200,
                    page.getTotalPages(), page.getTotalElements(), page.getNumber() + 1);
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ServiceResponse getLogDetail(Long id){
        try{
            ProductsLog log = productLogRepository.findById(id)
                   .orElseThrow(() -> new AppException(ErrorCode.IMPORT_TICKET_NOT_FOUND));
            ProductLogDTO dto = productMapper.fromEntityToLogDTO(log);
            return new ServiceResponse(dto, "Lấy thông tin thành công", 200);
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ServiceResponse exportLog(HttpServletResponse response, ExportLogRequest request){
        try {
            String fileName = excelExport.writeExcelLog(request);
            File file = new File(BASE_DIR + fileName);
            DownloadUtils.download(response, file);
            return new ServiceResponse("Xuất file Excel thành công", 200);
        }catch (IOException e){
            throw new AppException(ErrorCode.IO_EXCEPTION);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
