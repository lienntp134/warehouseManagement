package com.datn.warehousemgmt.processor;

import com.datn.warehousemgmt.dto.PacketDTO;
import com.datn.warehousemgmt.dto.ProductLogDTO;
import com.datn.warehousemgmt.dto.RfidDTO;
import com.datn.warehousemgmt.dto.ServiceResponse;
import com.datn.warehousemgmt.dto.response.ImportProcessResponse;
import com.datn.warehousemgmt.entities.*;
import com.datn.warehousemgmt.exception.AppException;
import com.datn.warehousemgmt.exception.ErrorCode;
import com.datn.warehousemgmt.mapper.ProductMapper;
import com.datn.warehousemgmt.service.*;
import com.datn.warehousemgmt.utils.Constant;
import com.datn.warehousemgmt.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImportGoodsProcessor {
    private final BatchService batchService;
    private final PacketService packetService;
    private final ProductService productService;
    private final ProductLogService productLogService;
    private final WareHouseService wareHouseService;
    private final UserUtils utils;
    private final ProductMapper productMapper;

    @Transactional
    public ServiceResponse executeTask(RfidDTO request){
        if(Objects.equals(request.getAction(), Constant.ProductLogAction.IMPORT.getAction())){
            return importGoods(request);
        }else if(Objects.equals(request.getAction(), Constant.ProductLogAction.EXPORT.getAction())){
            return exportGoods(request);
        }

        throw new AppException(ErrorCode.ACTION_NOT_DEFINE);
    }

    @Transactional
    public ServiceResponse importGoods(RfidDTO request){
        Users myAccount = utils.getMyUser();
        ImportProcessResponse response = new ImportProcessResponse();
        verifyImportGoodRequest(request);
        BatchProduct batchProduct = batchService.getBatchById(request.getBatchId());
        Optional<ProductsLog> oProductsLog = productLogService.findByStatusActionAndBatchId(
                Constant.ProductLogStatus.PENDING.name(),
                Constant.ProductLogAction.IMPORT.name(),
                request.getBatchId()
        );

        // Create or Import Batch
//        BatchProduct batchProduct;
        ProductsLog productsLog = new ProductsLog();
        ProductLogDTO productLogDTO = new ProductLogDTO();
        if(batchProduct == null){
            batchProduct = new BatchProduct();
            batchProduct.setId(request.getBatchId());
            batchProduct.setProductSkuCode(request.getSkuCode());
            batchProduct.setManufacturerDate(request.getManufacturerDate());
            batchProduct.setExpirationDate(request.getExpirationDate());
            batchProduct.setWarehouseId(utils.getMyUser().getWarehouseId());
            batchProduct.setQuantity(1);
        }else {
            if (!batchProduct.getManufacturerDate().equals(request.getManufacturerDate()) ||
                    !batchProduct.getExpirationDate().equals(request.getExpirationDate()) ||
                    !Objects.equals(batchProduct.getProductSkuCode(), request.getSkuCode())) {
                throw new AppException(ErrorCode.BATCH_INFORMATION_NOT_MATCH);
            }
            batchProduct.setQuantity(batchProduct.getQuantity() + 1);
        }
        batchProduct = batchService.save(batchProduct);

        if(!oProductsLog.isPresent()){
            productLogDTO.setBatchProductId(batchProduct.getId());
            productLogDTO.setAction(Constant.ProductLogAction.IMPORT.name());
            productLogDTO.setStatus(Constant.ProductLogStatus.PENDING.name());
            productsLog = productLogService.createLog(productLogDTO);
        }else{
            productsLog = oProductsLog.get();
        }
        productLogDTO = productMapper.fromEntityToLogDTO(productsLog);
        // Import Packet
        PacketDTO packetDTO = new PacketDTO();
        packetDTO.setRfidTag(request.getRfidCode());
        packetDTO.setBatchId(batchProduct.getId());
        packetDTO.setQuantity(request.getQuantity());
        packetDTO.setStatus(Constant.PacketStatus.IN_STOCK.getStatus());
        Packet packet = (Packet) packetService.importPacket(packetDTO).getData();

        // Write Log
        productLogDTO.getPackets().add(packet);
        productLogDTO.setQuantity(productLogDTO.getQuantity() + 1);
        productsLog = productLogService.updateLog(productLogDTO);

        response.setBatchId(batchProduct.getId());
        response.setImportDate(LocalDate.from(productsLog.getCreatedDate()));
        response.setSkuCode(request.getSkuCode());
        response.setManufacturerDate(batchProduct.getManufacturerDate());
        response.setExpirationDate(batchProduct.getExpirationDate());
        response.setQuantity(batchProduct.getQuantity());
        response.setWarehouseName(wareHouseService.getById(myAccount.getWarehouseId()).getName());

        return new ServiceResponse(response,"Nhập kho thành công", 200);
    }

    @Transactional
    public ServiceResponse exportGoods(RfidDTO request){
        Users myAccount = utils.getMyUser();
        ImportProcessResponse response = new ImportProcessResponse();
        verifyExportGoodRequest(request);
        // Change Packet Status
        Packet packet = (Packet) packetService.exportPacket(request.getRfidCode()).getData();
        BatchProduct batchProduct = batchService.getBatchById(packet.getBatchId());
        Optional<ProductsLog> oProductsLog = productLogService.findByStatusActionAndBatchId(
                Constant.ProductLogStatus.PENDING.name(),
                Constant.ProductLogAction.EXPORT.name(),
                packet.getBatchId()
        );

        // Create or Import Batch
//        BatchProduct batchProduct;
        ProductsLog productsLog = new ProductsLog();
        ProductLogDTO productLogDTO = new ProductLogDTO();
        if(batchProduct == null){
            throw new AppException(ErrorCode.BATCH_NOT_FOUND);
        }else {
            batchProduct.setQuantity(batchProduct.getQuantity() - 1);
        }
        batchProduct = batchService.save(batchProduct);

        if(oProductsLog.isEmpty()){
            productLogDTO.setBatchProductId(batchProduct.getId());
            productLogDTO.setAction(Constant.ProductLogAction.EXPORT.name());
            productLogDTO.setStatus(Constant.ProductLogStatus.PENDING.name());
            productsLog = productLogService.createLog(productLogDTO);
        }else{
            productsLog = oProductsLog.get();
        }
        productLogDTO = productMapper.fromEntityToLogDTO(productsLog);


        // Write Log
        productLogDTO.getPackets().add(packet);
        productLogDTO.setQuantity(productLogDTO.getQuantity() + 1);
        productsLog = productLogService.updateLog(productLogDTO);

        response.setBatchId(batchProduct.getId());
        response.setImportDate(LocalDate.from(productsLog.getCreatedDate()));
        response.setSkuCode(request.getSkuCode());
        response.setManufacturerDate(batchProduct.getManufacturerDate());
        response.setExpirationDate(batchProduct.getExpirationDate());
        response.setQuantity(batchProduct.getQuantity());
        response.setWarehouseName(wareHouseService.getById(myAccount.getWarehouseId()).getName());

        return new ServiceResponse(response,"Xuất kho thành công", 200);
    }

    private void verifyImportGoodRequest(RfidDTO request){
        if(request == null || request.getSkuCode() == null || request.getBatchId() == null){
            throw new AppException(ErrorCode.INVALID_RFID_TAG);
        }

        if(!productService.existProductBySKUCode(request.getSkuCode())){
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        if(packetService.checkPacket(request.getRfidCode())){
            throw new AppException(ErrorCode.PACKET_ALREADY_IMPORTED);
        }

    }

    private void verifyExportGoodRequest(RfidDTO request){
        if(request == null || request.getRfidCode() == null){
            throw new AppException(ErrorCode.INVALID_RFID_TAG);
        }
    }
}
