package projectsem4.medcare_server.service.admin;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IBooking.BookingRes;
import projectsem4.medcare_server.interfaces.admin.ITransaction;
import projectsem4.medcare_server.repository.admin.TransactionRepository;

@Service
public class TransactionService implements ITransaction {

  @Autowired
  TransactionRepository _tranRepo;

  @Override
  public CustomResult GetAll(Filter res) {
    try {
      int page = res.getPage() != null ? res.getPage() : 0;
      int size = res.getSize() != null ? res.getSize() : 10;
      Page<TransactionRes> transactionPage = _tranRepo.findAllByFilter(
          res.getType(),
          res.getStartDate(),
          res.getEndDate(),
          PageRequest.of(page, size));
      return new CustomResult(200, "Get Transaction Success", transactionPage);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult ExportFile(Filter res) {
    try {

      List<TransactionRes> rs = _tranRepo.exportExcel(
          res.getType(),
          res.getStartDate(),
          res.getEndDate());

      Workbook workbook = new XSSFWorkbook();
      Sheet sheet = workbook.createSheet("Bookings");

      // Tạo kiểu cho header
      CellStyle headerStyle = workbook.createCellStyle();
      Font headerFont = workbook.createFont();
      headerFont.setBold(true); // In đậm
      headerFont.setFontHeightInPoints((short) 12); // Font size lớn hơn
      headerFont.setColor(IndexedColors.WHITE.getIndex());
      headerStyle.setFont(headerFont);
      headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex()); // Màu xanh
      headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      headerStyle.setAlignment(HorizontalAlignment.CENTER); // Canh giữa
      headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

      // Tạo kiểu cho data (mặc định)
      CellStyle dataStyle = workbook.createCellStyle();
      dataStyle.setWrapText(true); // Text wrap cho cột mô tả dài
      dataStyle.setVerticalAlignment(VerticalAlignment.TOP);

      // Tạo header row
      Row header = sheet.createRow(0);
      String[] headers = { "ID", "Traded", "Balance", "Description", "CreateDate", "updateDate", "Status", "Email" };

      for (int i = 0; i < headers.length; i++) {
        Cell cell = header.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerStyle);
      }

      // Viết dữ liệu vào Excel
      int rowIndex = 1;
      for (TransactionRes trans : rs) {
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue(trans.getId());
        row.createCell(1).setCellValue(trans.getTraded());
        row.createCell(2).setCellValue(trans.getBalance());
        row.createCell(3).setCellValue(trans.getDescription());
        row.createCell(4).setCellValue(trans.getCreateDate().toString());
        row.createCell(5).setCellValue(trans.getUpdateDate().toString());
        row.createCell(6).setCellValue(trans.getStatus());
        row.createCell(7).setCellValue(trans.getEmail());
      }

      // Điều chỉnh độ rộng cột tự động, ngoại trừ cột mô tả
      for (int i = 0; i < headers.length; i++) {
        if (i != 3) { // Không auto-size cho cột Description
          sheet.autoSizeColumn(i);
        } else {
          sheet.setColumnWidth(i, 10000); // Đặt cố định width cho cột Description
        }
      }

      // Ghi file vào ByteArrayOutputStream
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      workbook.write(out);
      workbook.close();

      // Mã hóa file Excel sang Base64
      String base64File = Base64.getEncoder().encodeToString(out.toByteArray());

      // Đóng gói file Excel trong CustomResult
      return new CustomResult(200, "File exported successfully", base64File);

    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

}
