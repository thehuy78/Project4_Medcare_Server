package projectsem4.medcare_server.service.admin;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
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

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Booking;
import projectsem4.medcare_server.interfaces.admin.IAccountant;
import projectsem4.medcare_server.interfaces.admin.IBooking.BookingRes;
import projectsem4.medcare_server.repository.admin.AccountantRepository;

@Service
public class AccountantService implements IAccountant {

  @Autowired
  private AccountantRepository accountantRepository;

  @Override
  public CustomResult GetWeekAccountant(String hospitalId) {
    try {
      Calendar calendar = Calendar.getInstance();

      // Tìm ngày bắt đầu tuần (Chủ Nhật)
      calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      Date startDate = calendar.getTime();

      // Tìm ngày kết thúc tuần (Thứ Bảy)
      calendar.add(Calendar.DATE, 6); // Thêm 6 ngày để tới Thứ Bảy
      calendar.set(Calendar.HOUR_OF_DAY, 23);
      calendar.set(Calendar.MINUTE, 59);
      calendar.set(Calendar.SECOND, 59);
      calendar.set(Calendar.MILLISECOND, 999);
      Date endDate = calendar.getTime();

      // Lấy danh sách booking trong tuần
      List<Booking> bookings = accountantRepository.findBookingsInWeek(startDate, endDate);

      // Tính tổng revenue và profit
      Double totalRevenue = 0.0;
      Double totalProfit = 0.0;

      for (Booking booking : bookings) {
        totalRevenue += booking.getRevenue();
        totalProfit += booking.getProfit();
      }

      List<Double> result = Arrays.asList(totalRevenue, totalProfit);

      return new CustomResult(200, "Get Success", result);

    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult GetMonthAccountant(String hospitalId) {
    try {
      Calendar calendar = Calendar.getInstance();

      // Tìm ngày bắt đầu của tháng (ngày 1)
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      Date startDate = calendar.getTime();

      // Tìm ngày kết thúc của tháng (ngày cuối cùng của tháng)
      calendar.add(Calendar.MONTH, 1); // Chuyển sang tháng kế tiếp
      calendar.set(Calendar.DAY_OF_MONTH, 1); // Đặt về ngày đầu tiên của tháng kế tiếp
      calendar.add(Calendar.DATE, -1); // Lùi lại 1 ngày để có ngày cuối cùng của tháng hiện tại
      calendar.set(Calendar.HOUR_OF_DAY, 23);
      calendar.set(Calendar.MINUTE, 59);
      calendar.set(Calendar.SECOND, 59);
      calendar.set(Calendar.MILLISECOND, 999);
      Date endDate = calendar.getTime();

      // Lấy danh sách booking trong tháng
      List<Booking> bookings = accountantRepository.findBookingsInWeek(startDate, endDate);

      // Tính tổng revenue và profit
      Double totalRevenue = 0.0;
      Double totalProfit = 0.0;

      for (Booking booking : bookings) {
        totalRevenue += booking.getRevenue();
        totalProfit += booking.getProfit();
      }

      List<Double> result = Arrays.asList(totalRevenue, totalProfit);

      return new CustomResult(200, "Get Success", result);

    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  public List<BookingRes> GetBooking(FilterAccountant filterRes) {
    try {
      Calendar calendar = Calendar.getInstance();

      Date startDate = calendar.getTime(); // Get start date
      Date endDate = calendar.getTime(); // Get end date
      switch (filterRes.getTime()) {
        case 7:

          // Tìm ngày bắt đầu tuần (Chủ Nhật)
          calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
          calendar.set(Calendar.HOUR_OF_DAY, 0);
          calendar.set(Calendar.MINUTE, 0);
          calendar.set(Calendar.SECOND, 0);
          calendar.set(Calendar.MILLISECOND, 0);
          startDate = calendar.getTime();

          // Tìm ngày kết thúc tuần (Thứ Bảy)
          calendar.add(Calendar.DATE, 6); // Thêm 6 ngày để tới Thứ Bảy
          calendar.set(Calendar.HOUR_OF_DAY, 23);
          calendar.set(Calendar.MINUTE, 59);
          calendar.set(Calendar.SECOND, 59);
          calendar.set(Calendar.MILLISECOND, 999);
          endDate = calendar.getTime();
          break;
        case 30:

          // Tìm ngày bắt đầu của tháng (ngày 1)
          calendar.set(Calendar.DAY_OF_MONTH, 1);
          calendar.set(Calendar.HOUR_OF_DAY, 0);
          calendar.set(Calendar.MINUTE, 0);
          calendar.set(Calendar.SECOND, 0);
          calendar.set(Calendar.MILLISECOND, 0);
          startDate = calendar.getTime();

          // Tìm ngày kết thúc của tháng (ngày cuối cùng của tháng)
          calendar.add(Calendar.MONTH, 1); // Chuyển sang tháng kế tiếp
          calendar.set(Calendar.DAY_OF_MONTH, 1); // Đặt về ngày đầu tiên của tháng kế tiếp
          calendar.add(Calendar.DATE, -1); // Lùi lại 1 ngày để có ngày cuối cùng của tháng hiện tại
          calendar.set(Calendar.HOUR_OF_DAY, 23);
          calendar.set(Calendar.MINUTE, 59);
          calendar.set(Calendar.SECOND, 59);
          calendar.set(Calendar.MILLISECOND, 999);
          endDate = calendar.getTime();
          break;

        default:
          break;
      }

      List<BookingRes> bookingsPage = accountantRepository.findByFilters(
          filterRes.getHospitalCode(),
          startDate, // Pass start date
          endDate);
      ;

      return bookingsPage;

    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public CustomResult ExportFile(FilterAccountant filterRes) {
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
      String[] headers = { "ID", "Booking Date", "Booking Time", "Revenue", "Profit", "Hospital", "Type",
          "Email User", "Name", "Phone", "Gender", "Dob", "Province", "Identifier", "Description", "Status",
          "Create Date", "Update Date" };

      for (int i = 0; i < headers.length; i++) {
        Cell cell = header.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerStyle);
      }

      // Viết dữ liệu vào Excel
      int rowIndex = 1;
      Double totalRevenue = 0.0;
      Double totalProfit = 0.0;

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
        row.createCell(14).setCellValue(booking.getDescription());
        row.createCell(15).setCellValue(booking.getStatus());
        row.createCell(16).setCellValue(booking.getCreateDate().toString());
        row.createCell(17).setCellValue(booking.getUpdateDate().toString());

        // Tính tổng revenue và profit
        totalRevenue += booking.getRevenue();
        totalProfit += booking.getProfit();
      }

      for (int i = 0; i < headers.length; i++) {
        if (!headers[i].equals("Description")) { // Nếu không phải cột "Description"
          sheet.autoSizeColumn(i); // Tự động điều chỉnh kích thước
        }
      }

      // Đặt kích thước cố định cho cột "Description" (nếu cần)
      int descriptionColumnIndex = 14; // Cột "Description" là cột thứ 14 (bắt đầu từ 0)
      sheet.setColumnWidth(descriptionColumnIndex, 10000); // Đặt độ rộng cố định (tính bằng

      // Tạo bảng Summary bên cạnh
      int summaryStartColumn = headers.length + 2; // Cột bắt đầu của Summary (cách 2 cột)
      int summaryStartRow = 0; // Bảng Summary bắt đầu từ dòng đầu tiên

      // Header của bảng Summary
      Row summaryHeaderRow = sheet.getRow(summaryStartRow); // Lấy dòng đầu tiên (header)
      if (summaryHeaderRow == null) {
        summaryHeaderRow = sheet.createRow(summaryStartRow); // Nếu dòng chưa tồn tại, tạo dòng mới
      }
      Cell summaryHeaderCell = summaryHeaderRow.createCell(summaryStartColumn);
      summaryHeaderCell.setCellValue("Summary");
      summaryHeaderCell.setCellStyle(headerStyle);

      // Dòng đầu tiên của bảng Summary (Total Revenue)
      Row summaryRow1 = sheet.getRow(summaryStartRow + 1); // Dòng thứ hai
      if (summaryRow1 == null) {
        summaryRow1 = sheet.createRow(summaryStartRow + 1); // Tạo dòng mới nếu chưa tồn tại
      }
      summaryRow1.createCell(summaryStartColumn).setCellValue("Total Revenue");
      summaryRow1.createCell(summaryStartColumn + 1).setCellValue(totalRevenue);

      // Dòng thứ hai của bảng Summary (Total Profit)
      Row summaryRow2 = sheet.getRow(summaryStartRow + 2); // Dòng thứ ba
      if (summaryRow2 == null) {
        summaryRow2 = sheet.createRow(summaryStartRow + 2); // Tạo dòng mới nếu chưa tồn tại
      }
      summaryRow2.createCell(summaryStartColumn).setCellValue("Total Profit");
      summaryRow2.createCell(summaryStartColumn + 1).setCellValue(totalProfit);

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
