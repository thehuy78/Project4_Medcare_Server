package projectsem4.medcare_server.service.admin;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IBooking.BookingRes;
import projectsem4.medcare_server.interfaces.admin.IDoctor;
import projectsem4.medcare_server.interfaces.admin.IPack;
import projectsem4.medcare_server.interfaces.admin.IReport;
import projectsem4.medcare_server.interfaces.admin.ITesting;
import projectsem4.medcare_server.interfaces.admin.IVaccine;

import projectsem4.medcare_server.repository.admin.ReportRepository;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Cell;

@Service
public class ReportService implements IReport {
  @Autowired
  ReportRepository reportRepo;

  @Autowired
  IDoctor _IDoctor;
  @Autowired
  IPack _IPack;
  @Autowired
  ITesting _ITesting;
  @Autowired
  IVaccine _IVaccine;

  public List<BookingRes> GetBooking(FilterRes filterRes) {
    try {

      Double revenueStart = null;
      Double revenueEnd = null;
      Date startDate = filterRes.getStartDate(); // Get start date
      Date endDate = filterRes.getEndDate(); // Get end date

      if (filterRes.getRevenue() != null && filterRes.getRevenue().size() == 2) {
        revenueStart = filterRes.getRevenue().get(0); // Kiểm tra xem có phần tử đầu tiên không
        revenueEnd = filterRes.getRevenue().get(1); // Kiểm tra xem có phần tử thứ hai không
      } else {
        revenueStart = null; // Nếu không đủ phần tử, đặt về null
        revenueEnd = null; // Nếu không đủ phần tử, đặt về null
      }
      List<BookingRes> bookingsPage;
      switch (filterRes.getTypeUrl()) {
        case "":
          bookingsPage = reportRepo.findByFilters(
              filterRes.getSearch(),
              filterRes.getGender(),
              filterRes.getType(),
              filterRes.getHospitalCode(),
              revenueStart, // Truyền feeStart
              revenueEnd, // Truyền feeEnd
              startDate, // Pass start date
              endDate);
          break;
        case "doctor":
          bookingsPage = reportRepo.findByFiltersDoctor(
              filterRes.getSearch(),
              filterRes.getGender(),
              filterRes.getType(),
              filterRes.getTypeUrl(),
              filterRes.getIdUrl(),
              revenueStart, // Truyền feeStart
              revenueEnd, // Truyền feeEnd
              startDate, // Pass start date
              endDate);
          break;
        case "package":
          bookingsPage = reportRepo.findByFiltersPack(
              filterRes.getSearch(),
              filterRes.getGender(),
              filterRes.getType(),
              filterRes.getTypeUrl(),
              filterRes.getIdUrl(),
              revenueStart, // Truyền feeStart
              revenueEnd, // Truyền feeEnd
              startDate, // Pass start date
              endDate);
          break;
        case "testing":
          bookingsPage = reportRepo.findByFiltersTest(
              filterRes.getSearch(),
              filterRes.getGender(),
              filterRes.getType(),
              filterRes.getTypeUrl(),
              filterRes.getIdUrl(),
              revenueStart, // Truyền feeStart
              revenueEnd, // Truyền feeEnd
              startDate, // Pass start date
              endDate);
          break;
        case "vaccine":
          bookingsPage = reportRepo.findByFiltersVaccine(
              filterRes.getSearch(),
              filterRes.getGender(),
              filterRes.getType(),
              filterRes.getTypeUrl(),
              filterRes.getIdUrl(),
              revenueStart, // Truyền feeStart
              revenueEnd, // Truyền feeEnd
              startDate, // Pass start date
              endDate);
          break;

        default:
          return null;
      }
      return bookingsPage;

    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public CustomResult ExportFile(FilterRes filterRes) {
    try {
      List<BookingRes> rs = GetBooking(filterRes);

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
      String[] headers = { "ID", "Booking Date", "Booking Time", "Revenue", "Profit", "Hospital", "Type", "Email User",
          "Name", "Phone", "Gender", "Dob", "Province", "Identifier", "Description", "Status", "Create Date",
          "Update Date" };

      for (int i = 0; i < headers.length; i++) {
        Cell cell = header.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerStyle);
      }

      // Viết dữ liệu vào Excel
      int rowIndex = 1;
      for (BookingRes booking : rs) {
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue(booking.getId());
        row.createCell(1).setCellValue(booking.getBookingDate().toString());
        row.createCell(2).setCellValue(booking.getBookingTime());
        row.createCell(3).setCellValue(booking.getRevenue());
        row.createCell(4).setCellValue(booking.getProfit());
        row.createCell(5).setCellValue(booking.getHospitalName());
        row.createCell(6).setCellValue(booking.getTypeName());
        row.createCell(7).setCellValue(booking.getUserEmail());
        row.createCell(8).setCellValue(booking.getName());
        row.createCell(9).setCellValue(booking.getPhone());
        row.createCell(10).setCellValue(booking.getGender());
        row.createCell(11).setCellValue(booking.getDob().toString());
        row.createCell(12).setCellValue(booking.getProvince());
        row.createCell(13).setCellValue(booking.getIdentifier());
        row.createCell(14).setCellValue(booking.getDescription()); // Không format cột này
        row.createCell(15).setCellValue(booking.getStatus());
        row.createCell(16).setCellValue(booking.getCreateDate().toString());
        row.createCell(17).setCellValue(booking.getUpdateDate().toString());
      }

      // Điều chỉnh độ rộng cột tự động, ngoại trừ cột mô tả
      for (int i = 0; i < headers.length; i++) {
        if (i != 14) { // Không auto-size cho cột Description
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
