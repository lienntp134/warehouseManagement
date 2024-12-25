package com.datn.warehousemgmt.utils;

import com.datn.warehousemgmt.dto.request.ExportLogRequest;
import com.datn.warehousemgmt.repository.ProductLogRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExcelExport {

    @Autowired
    StyleExcel styleExcel;

    @Value("${excel.log.folder}")
    public String BASE_DIR;

    private final ProductLogRepository productLogRepository;
    private final UserUtils utils;

    // Auto resize column width
    public void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            if (columnIndex == 1) {
                sheet.setColumnWidth(1, 6800);
                continue;
            }
            sheet.autoSizeColumn(columnIndex);
        }
    }

    public void setTitle(XSSFSheet sheet, String content, CellRangeAddress cellRangeAddress, Integer indexRow, Integer indexCell, CellStyle cellStyle) {
        if (StringUtils.isNotBlank(content)) {
            XSSFRow rowTitle = sheet.getRow(indexRow);
            if (rowTitle == null) {
                rowTitle = sheet.createRow(indexRow); // Tạo dòng nếu chưa tồn tại
            }
            XSSFCell cell = rowTitle.createCell(indexCell);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(content);
            sheet.addMergedRegion(cellRangeAddress);
        }
    }

    // Create output file
    public void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
        File file = new File(StringUtils.substringBeforeLast(excelFilePath, "/"));
        if (!file.exists()) {
            file.mkdirs();
        }
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
        }
    }

    public void setCellContent(XSSFRow row,
                               String content, Integer colIndex, CellStyle cellStyle) {
        Cell cell = row.createCell(colIndex); // Chỉ tạo 1 ô tại colIndex
        cell.setCellStyle(cellStyle);
        cell.setCellValue(content); // Gán nội dung vào ô
    }

    public String writeExcelLog(ExportLogRequest request) throws IOException {
        Long warehouseId = null;
        if(!utils.isAdmin()){
            warehouseId = utils.getMyUser().getWarehouseId();
        }
        String nameFile = "Bao_cao_thong_ke_xuat_nhap_kho_" + LocalDate.now() + "_" + UUID.randomUUID() + ".xlsx";
        String path = BASE_DIR + nameFile;
        List<Object[]> res = productLogRepository.getLogExport(request.getSkuCode(),
                request.getBatchId(),
                request.getAction(),
                request.getStartDate(),
                request.getEndDate(),
                warehouseId);

        String name = "Báo cáo nhập/xuất kho";
        String timeCreate = "Ngày tạo báo cáo: " + DateTimeUtils.formatLocalDateTime(LocalDateTime.now());
        String time = "Khoảng thống kê: ";
        if (request.getStartDate() == null && request.getEndDate() == null) {
            time += "Toàn thời gian";
        } else if (request.getStartDate() != null && request.getEndDate() != null) {
            time += DateTimeUtils.formatLocalDateTime(request.getStartDate()) +
                    " - " + DateTimeUtils.formatLocalDateTime(request.getEndDate());
        } else if (request.getStartDate() != null && request.getEndDate() == null){
            time += "Từ ngày " + DateTimeUtils.formatLocalDateTime(request.getStartDate());
        }else{
            time += "Đến ngày " + DateTimeUtils.formatLocalDateTime(request.getEndDate());
        }

        List<String> titles = Arrays.asList("STT", "SKU", "Tên sản phẩm",
                "Mã lô", "Số lượng (kiện)", "Hành động",
                "Người phụ trách", "Thời gian nhập/xuất", "Mã đối tác",
                "Tên đối tác", "Chi phí");
        int maxColumn = titles.size();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Bao_cao");

        CellStyle firstTitle = StyleExcel.custom(sheet, "Times New Roman", (short) 13, true, false, false, null, null, true, true);
        CellStyle timeTitle = StyleExcel.custom(sheet, "Times New Roman", (short) 11, true, false, false, null, null, true, true);
        CellStyle header = StyleExcel.custom(sheet, "Calibri", (short) 11, true, true, true, null, null, false, true);
        CellStyle content = StyleExcel.custom(sheet, "Calibri", (short) 11, false, false, true, null, null, false, true);
        setTitle(sheet, name,
                new CellRangeAddress(0, 1, 0, maxColumn - 1), 0, 0,
                firstTitle);
        setTitle(sheet, timeCreate,
                new CellRangeAddress(2, 2, 0, maxColumn - 1), 2, 0,
                timeTitle);
        setTitle(sheet, time,
                new CellRangeAddress(3, 3, 0, maxColumn - 1), 3, 0,
                timeTitle);

        int rowNumber = 5;
        XSSFRow rowContent = sheet.createRow(rowNumber);
        int index = 1;

        for (int i = 0; i < titles.size(); i++) {
            setCellContent(rowContent, titles.get(i), i, header);
        }
        rowNumber++;

        if (!res.isEmpty()) {
            for (Object[] re : res) {
                XSSFRow dataRow = sheet.createRow(rowNumber);
                setCellContent(dataRow, String.valueOf(index), 0, content);
                for (int j = 1; j < re.length; j++) {
                    if (j == 7) {
                        Timestamp temp = (Timestamp) re[j];
                        LocalDateTime value = temp == null ? null : temp.toLocalDateTime();
                        setCellContent(dataRow, value == null ? "" : DateTimeUtils.formatLocalDateTime(value), j, content);
                        continue;
                    }
                    setCellContent(dataRow, String.valueOf(re[j] == null ? "" : re[j]), j, content);
                }
                rowNumber++;
                index++;
            }
        }

        autosizeColumn(sheet, maxColumn + 1);
        createOutputFile(workbook, path);
        return nameFile;
    }
}

