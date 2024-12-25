package com.datn.warehousemgmt.utils;

import com.datn.warehousemgmt.dto.ImportDetail;
import com.datn.warehousemgmt.dto.request.ProductRequest;
import com.datn.warehousemgmt.entities.Product;
import com.datn.warehousemgmt.exception.AppException;
import com.datn.warehousemgmt.exception.ErrorCode;
import com.datn.warehousemgmt.repository.CategoryRepository;
import com.datn.warehousemgmt.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportProduct {

    @Value("${upload.folder}")
    private String uploadFolder;

    @Value("${upload.result.folder}")
    private String uploadResultFolder;

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;
    final String[] HEADERS = {"SKUCode", "Name", "Description", "Category"};
    final String[] ERROR_HEADERS = {"SKUCode", "Name", "Error"};
    public ImportDetail readFileImportUserToCourse(String fileName){
        String path = uploadFolder + fileName;
        ImportDetail importDetail = new ImportDetail();
        List<ProductRequest> validProducts = new ArrayList<>();
        List<List<String>> errors = new ArrayList<>();
        Integer counter = 0;

        try (InputStream inputStream = new FileInputStream(path);
             BOMInputStream bomInputStream = new BOMInputStream(inputStream);
             Reader in = new InputStreamReader(bomInputStream, StandardCharsets.UTF_8)) {
             Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(false)
                    .build()
                    .parse(in);

            CSVRecord headerRecord = records.iterator().next();
            List<String> actualHeaders = new ArrayList<>(List.of(headerRecord.values()));
            List<String> headers = new ArrayList<>();
            Collections.addAll(headers, HEADERS);
            if (!headers.equals(actualHeaders)){
                throw new AppException(ErrorCode.FILE_FALSE_FORMAT);
            }

            for (CSVRecord record : records) {
                counter++;
                String skuCode = record.get("SKUCode");
                String name = record.get("Name");
                String description = record.get("Description");
                List<Long> categories;

                try {
                    categories = Arrays.stream(record.get("Category").split(","))
                            .map(String::trim)
                            .filter(s -> s.matches("\\d+"))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                } catch (NumberFormatException e) {
                    categories = new ArrayList<>();
                }

                // Validate data
                StringBuilder errorMessages = new StringBuilder();
                List<String> errorRecord = new ArrayList<>();
                errorRecord.add(skuCode);
                errorRecord.add(name);

                boolean isValid = true;
                if (skuCode == null || skuCode.isEmpty()) {
                    errorMessages.append("Mã sản phẩm không được để trống. ");
                    isValid = false;
                }
                if (productRepository.existsBySkuCode(skuCode)) {
                    errorMessages.append("Mã sản phẩm đã tồn tại trong hệ thống. ");
                    isValid = false;
                }
                if (name == null || name.isEmpty()) {
                    errorMessages.append("Tên sản phẩm không được để trống. ");
                    isValid = false;
                }

                if (isValid) {
                    validProducts.add(new ProductRequest(skuCode, name, description, null, categories, null, null));
                    errorMessages.append("Success");
                }

                errorRecord.add(errorMessages.toString());
                errors.add(errorRecord);

            }

            importDetail.setProducts(validProducts);
            importDetail.setError(errors);
            importDetail.setTotalRow(counter);

        } catch (FileNotFoundException e) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new AppException(ErrorCode.IO_EXCEPTION);
        }

        return importDetail;
    }

    @Transactional
    public void importProduct(ImportDetail importDetail){
        List<Product> productList = new ArrayList<>();
        try {
            importDetail.getProducts().forEach(dto -> {
                Product product = new Product();
                product.setSkuCode(dto.getSkuCode());
                product.setName(dto.getName());
                product.setDescription(dto.getDescription());
                product.getCategories().addAll(categoryRepository.findAllByIdIn(dto.getCategoryIds()));
                productList.add(product);
            });
            productRepository.saveAll(productList);
        }catch (Exception e){
            throw new AppException(ErrorCode.IMPORT_FAILED);
        }
    }

    public String fileErrorName(ImportDetail importDetail, String fileName){
        List<String> fileSplit = List.of(fileName.split("\\."));
        String originalName = fileName.split("-")[0];
        String outputPath = uploadResultFolder + originalName + "-" + UUID.randomUUID() + "." + fileSplit.get(1);

        try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath)){
            fileOutputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            try(
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    CSVPrinter csvPrinter = new CSVPrinter(bufferedWriter,
                            CSVFormat.DEFAULT.builder()
                                    .setHeader(ERROR_HEADERS)
                                    .setQuoteMode(QuoteMode.ALL)
                                    .build())) {
                for (List<String> error : importDetail.getError()) {
                    csvPrinter.printRecord(error);
                }
                csvPrinter.flush();
            }
        }catch (FileNotFoundException e) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new AppException(ErrorCode.IO_EXCEPTION);
        }

        return outputPath;

    }
}
