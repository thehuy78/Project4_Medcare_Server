package projectsem4.medcare_server.service.admin;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IChart;

import projectsem4.medcare_server.repository.admin.ChartRepository;
import projectsem4.medcare_server.repository.admin.TypeRepository;

@Service
public class ChartService implements IChart {

  @Autowired
  ChartRepository _ChartRepository;

  @Autowired
  TypeRepository _TypeRepo;

  @Override
  public CustomResult TotalRevenue() {
    try {
      Date startDayToday;
      Date endDayToday;
      Date startDayYesterday;
      Date endDayYesterday;
      Calendar cal = Calendar.getInstance();
      // Xác định thời gian cho ngày hiện tại
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      startDayToday = cal.getTime();

      cal.set(Calendar.HOUR_OF_DAY, 23);
      cal.set(Calendar.MINUTE, 59);
      cal.set(Calendar.SECOND, 59);
      cal.set(Calendar.MILLISECOND, 999);
      endDayToday = cal.getTime();

      // Xác định thời gian cho ngày hôm trước
      cal.add(Calendar.DAY_OF_MONTH, -1); // Giảm 1 ngày
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      startDayYesterday = cal.getTime();
      cal.set(Calendar.HOUR_OF_DAY, 23);
      cal.set(Calendar.MINUTE, 59);
      cal.set(Calendar.SECOND, 59);
      cal.set(Calendar.MILLISECOND, 999);
      endDayYesterday = cal.getTime();
      // Lấy doanh thu cho ngày hiện tại và ngày hôm trước
      var totalToday = _ChartRepository.TotalRevenueByDay(startDayToday, endDayToday);
      var totalYesterday = _ChartRepository.TotalRevenueByDay(startDayYesterday, endDayYesterday);
      // Tính phần trăm thay đổi
      Double todayRevenue = totalToday.getTotalRevenue();
      Double yesterdayRevenue = totalYesterday.getTotalRevenue();
      Double todayProfit = totalToday.getTotalProfit();
      Double yesterdayProfit = totalYesterday.getTotalProfit();
      Double percentChangeRevenue = null;
      if (yesterdayRevenue == 0 && todayRevenue == 0) {
        percentChangeRevenue = 0.0;
      } else if (todayRevenue > 0 && yesterdayRevenue == 0) {
        percentChangeRevenue = 100.0; // Nếu ngày hôm qua không có doanh thu, coi như tăng 100%
      } else if (todayRevenue == 0 && yesterdayRevenue > 0) {
        percentChangeRevenue = -100.0; // Nếu ngày hôm qua không có doanh thu, coi như tăng 100%
      } else {
        percentChangeRevenue = ((todayRevenue - yesterdayRevenue) / yesterdayRevenue) * 100;
      }
      Double percentChangeProfit = null;
      if (yesterdayProfit == 0 && todayProfit == 0) {
        percentChangeProfit = 0.0;
      } else if (todayProfit > 0 && yesterdayProfit == 0) {
        percentChangeProfit = 100.0; // Nếu ngày hôm qua không có doanh thu, coi như tăng 100%
      } else if (todayProfit == 0 && yesterdayProfit > 0) {
        percentChangeProfit = -100.0; // Nếu ngày hôm qua không có doanh thu, coi như tăng 100%
      } else {
        percentChangeProfit = ((todayProfit - yesterdayProfit) / yesterdayProfit) * 100;
      }
      TotalRevenueRespon resultToday = new TotalRevenueRespon(todayRevenue, todayProfit, percentChangeRevenue,
          percentChangeProfit);
      return new CustomResult(200, "ok", resultToday);

    } catch (Exception e) {
      return new CustomResult(400, "filter error", e.getMessage());
    }
  }

  @Override
  public CustomResult TotalRevenueHospital(String code) {
    try {
      Date startDayToday;
      Date endDayToday;
      Date startDayYesterday;
      Date endDayYesterday;
      Calendar cal = Calendar.getInstance();
      // Xác định thời gian cho ngày hiện tại
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      startDayToday = cal.getTime();

      cal.set(Calendar.HOUR_OF_DAY, 23);
      cal.set(Calendar.MINUTE, 59);
      cal.set(Calendar.SECOND, 59);
      cal.set(Calendar.MILLISECOND, 999);
      endDayToday = cal.getTime();

      // Xác định thời gian cho ngày hôm trước
      cal.add(Calendar.DAY_OF_MONTH, -1); // Giảm 1 ngày
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      startDayYesterday = cal.getTime();
      cal.set(Calendar.HOUR_OF_DAY, 23);
      cal.set(Calendar.MINUTE, 59);
      cal.set(Calendar.SECOND, 59);
      cal.set(Calendar.MILLISECOND, 999);
      endDayYesterday = cal.getTime();
      // Lấy doanh thu cho ngày hiện tại và ngày hôm trước
      var totalToday = _ChartRepository.TotalRevenueByDayHospital(startDayToday, endDayToday, code);
      var totalYesterday = _ChartRepository.TotalRevenueByDayHospital(startDayYesterday, endDayYesterday, code);
      // Tính phần trăm thay đổi
      Double todayRevenue = totalToday.getTotalRevenue();
      Double yesterdayRevenue = totalYesterday.getTotalRevenue();
      Double todayProfit = totalToday.getTotalProfit();
      Double yesterdayProfit = totalYesterday.getTotalProfit();
      Double percentChangeRevenue = null;
      if (yesterdayRevenue == 0 && todayRevenue == 0) {
        percentChangeRevenue = 0.0;
      } else if (todayRevenue > 0 && yesterdayRevenue == 0) {
        percentChangeRevenue = 100.0; // Nếu ngày hôm qua không có doanh thu, coi như tăng 100%
      } else if (todayRevenue == 0 && yesterdayRevenue > 0) {
        percentChangeRevenue = -100.0; // Nếu ngày hôm qua không có doanh thu, coi như tăng 100%
      } else {
        percentChangeRevenue = ((todayRevenue - yesterdayRevenue) / yesterdayRevenue) * 100;
      }
      // Double percentChangeProfit = null;
      // if (yesterdayProfit == 0 && todayProfit == 0) {
      // percentChangeProfit = 0.0;
      // } else if (todayProfit > 0 && yesterdayProfit == 0) {
      // percentChangeProfit = 100.0; // Nếu ngày hôm qua không có doanh thu, coi như
      // tăng 100%
      // } else if (todayProfit == 0 && yesterdayProfit > 0) {
      // percentChangeProfit = -100.0; // Nếu ngày hôm qua không có doanh thu, coi như
      // tăng 100%
      // } else {
      // percentChangeProfit = ((todayProfit - yesterdayProfit) / yesterdayProfit) *
      // 100;
      // }
      TotalRevenueRespon resultToday = new TotalRevenueRespon(todayRevenue, 0.0, percentChangeRevenue,
          0.0);
      return new CustomResult(200, "ok", resultToday);

    } catch (Exception e) {
      return new CustomResult(400, "filter error", e.getMessage());
    }
  }

  @Override
  public CustomResult TotalGenderBookingByDay(Integer day) {
    try {
      Date startDayToday;
      Date endDayToday;
      Calendar cal = Calendar.getInstance();
      if (day == 1) {
        // Xác định thời gian cho ngày hiện tại
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        startDayToday = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        endDayToday = cal.getTime();
        var totalGender = _ChartRepository.TotalGenderByDay(startDayToday, endDayToday);
        return new CustomResult(200, "ok", totalGender);
      } else if (day == 7) {
        // Xác định thời gian cho ngày hôm trước
        ; // Giảm 7 ngày
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -7);
        startDayToday = cal.getTime();
        // Giảm 7 ngày
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.add(Calendar.DAY_OF_MONTH, +7);
        endDayToday = cal.getTime();
        var totalGender = _ChartRepository.TotalGenderByDay(startDayToday, endDayToday);
        return new CustomResult(200, "7", totalGender);
      } else {
        return new CustomResult(400, "filter error", null);
      }
    } catch (Exception e) {
      return new CustomResult(400, "filter error", e.getMessage());
    }
  }

  @Override
  public CustomResult CountBookingByHours() {
    // Lấy ngày hiện tại
    Calendar cal = Calendar.getInstance();
    Date today = cal.getTime();
    // Tính ngày hôm qua
    cal.add(Calendar.DATE, -1);
    Date yesterday = cal.getTime();
    List<TotalBookingByHoursRes> todayResults = fillMissingHours(
        _ChartRepository.countBookingsByHour(today));
    List<TotalBookingByHoursRes> yesterdayResults = fillMissingHours(
        _ChartRepository.countBookingsByHour(yesterday));
    List<List<TotalBookingByHoursRes>> result = new ArrayList<>();
    result.add(todayResults);
    result.add(yesterdayResults);
    return new CustomResult(200, "ok", result);
  }

  // Hàm phụ để đảm bảo danh sách đủ 24 giờ
  private List<TotalBookingByHoursRes> fillMissingHours(List<TotalBookingByHoursRes> results) {
    Map<Integer, Long> bookingCountByHour = new HashMap<>();
    // Lưu các kết quả vào Map theo giờ
    for (TotalBookingByHoursRes res : results) {
      bookingCountByHour.put(res.getHour(), res.getCount());
    }
    // Tạo danh sách với đủ 24 giờ (0-23), count = 0 nếu giờ không có booking
    List<TotalBookingByHoursRes> fullDayResults = new ArrayList<>();
    for (int hour = 0; hour < 24; hour++) {
      fullDayResults.add(
          new TotalBookingByHoursRes(hour, bookingCountByHour.getOrDefault(hour, 0L)));
    }
    return fullDayResults;
  }

  @Override
  public CustomResult CountBookingByAge(Integer day) {
    try {
      Date startDayToday;
      Date endDayToday;
      Calendar cal = Calendar.getInstance();
      if (day == 1) {
        // Xác định thời gian cho ngày hiện tại
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        startDayToday = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        endDayToday = cal.getTime();
        var totalGender = _ChartRepository.countBookingsByAgeGroup(startDayToday, endDayToday);
        return new CustomResult(200, "ok", totalGender);
      } else if (day == 7) {
        // Xác định thời gian cho ngày hôm trước
        ; // Giảm 7 ngày
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -7);
        startDayToday = cal.getTime();
        // Giảm 7 ngày
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.add(Calendar.DAY_OF_MONTH, +7);
        endDayToday = cal.getTime();
        var totalGender = _ChartRepository.countBookingsByAgeGroup(startDayToday, endDayToday);
        return new CustomResult(200, "7", totalGender);
      } else {
        return new CustomResult(400, "filter error", null);
      }
    } catch (Exception e) {
      return new CustomResult(400, "filter error", e.getMessage());
    }
  }

  @Override
  public CustomResult countBookingsForWeek(Integer days) {
    try {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);

      String[] dayNames = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
      List<BookingCountByDay> currentPeriodCounts = new ArrayList<>();
      List<BookingCountByDay> previousPeriodCounts = new ArrayList<>();

      // Khởi tạo mảng để lưu tổng số booking theo thứ trong tuần
      Long[] currentWeekTotal = new Long[7];
      Long[] previousWeekTotal = new Long[7];
      Arrays.fill(currentWeekTotal, 0L);
      Arrays.fill(previousWeekTotal, 0L);

      // Lặp qua số ngày đã chỉ định
      for (int i = 0; i < days; i++) {
        Date currentDay = cal.getTime();
        Long count = _ChartRepository.countBookingsByDate(currentDay);
        int dayOfWeekIndex = (cal.get(Calendar.DAY_OF_WEEK) - 1); // Chỉ số thứ trong tuần (0-6)

        // Cộng dồn số lượng booking cho tuần hiện tại
        currentWeekTotal[dayOfWeekIndex] += (count != null) ? count : 0;

        // Lùi lại một ngày
        cal.add(Calendar.DAY_OF_MONTH, -1);
      }

      // Tính cho tuần trước
      for (int i = 0; i < days; i++) {
        Date previousDay = cal.getTime();
        Long count = _ChartRepository.countBookingsByDate(previousDay);
        int dayOfWeekIndex = (cal.get(Calendar.DAY_OF_WEEK) - 1); // Chỉ số thứ trong tuần (0-6)

        // Cộng dồn số lượng booking cho tuần trước
        previousWeekTotal[dayOfWeekIndex] += (count != null) ? count : 0;

        // Lùi lại một ngày
        cal.add(Calendar.DAY_OF_MONTH, -1);
      }

      // Chuyển đổi mảng tổng số booking sang danh sách BookingCountByDay
      for (int i = 0; i < 7; i++) {
        currentPeriodCounts.add(new BookingCountByDay(dayNames[i], currentWeekTotal[i]));
        previousPeriodCounts.add(new BookingCountByDay(dayNames[i], previousWeekTotal[i]));
      }

      // Tạo đối tượng kết quả chứa cả hai danh sách
      Map<String, List<BookingCountByDay>> resultMap = new HashMap<>();
      resultMap.put("currentPeriod", currentPeriodCounts);
      resultMap.put("previousPeriod", previousPeriodCounts);

      return new CustomResult(200, "ok", resultMap);
    } catch (Exception e) {
      return new CustomResult(400, "filter error", e.getMessage());
    }
  }

  @Override
  public CustomResult countTopHospital(Integer days) {
    try {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.HOUR_OF_DAY, 23);
      cal.set(Calendar.MINUTE, 59);
      cal.set(Calendar.SECOND, 59);
      cal.set(Calendar.MILLISECOND, 999);
      Date endDate = cal.getTime();
      Date startDate = null;

      switch (days) {
        case 1:
          cal.set(Calendar.HOUR_OF_DAY, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MILLISECOND, 0);
          startDate = cal.getTime();
          break;
        case 7:
          cal.add(Calendar.DAY_OF_MONTH, -6);
          cal.set(Calendar.HOUR_OF_DAY, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MILLISECOND, 0);
          startDate = cal.getTime();
          break;
        case 30:
          cal.add(Calendar.DAY_OF_MONTH, -29);
          cal.set(Calendar.HOUR_OF_DAY, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MILLISECOND, 0);
          startDate = cal.getTime();
          break;
        case 365:
          cal.add(Calendar.YEAR, -1);
          cal.set(Calendar.HOUR_OF_DAY, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MILLISECOND, 0);
          startDate = cal.getTime();
          break;
        default:
          throw new IllegalArgumentException("Invalid days parameter");
      }

      // Lấy danh sách tất cả bệnh viện cùng với số lượng booking
      List<TopHospitalRespon> results = _ChartRepository.countBookingsByTopHospital(startDate, endDate);
      Long total = _ChartRepository.countTotalBookings(startDate, endDate);

      // Nếu kết quả null, khởi tạo giá trị mặc định
      List<TopHospitalRespon> topHospitals = (results != null && results.size() > 5) ? results.subList(0, 5) : results;
      if (topHospitals == null)
        topHospitals = new ArrayList<>();

      // Chuẩn bị kết quả trả về
      Map<String, Object> rs = new HashMap<>();
      rs.put("top", topHospitals);
      rs.put("total", total != null ? total : 0);

      return new CustomResult(200, "ok", rs);
    } catch (Exception e) {
      e.printStackTrace();
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult countBookingByType(Integer days) {
    try {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.HOUR_OF_DAY, 23);
      cal.set(Calendar.MINUTE, 59);
      cal.set(Calendar.SECOND, 59);
      cal.set(Calendar.MILLISECOND, 999);
      Date endDate = cal.getTime();
      Date startDate = null;

      switch (days) {
        case 1:
          cal.set(Calendar.HOUR_OF_DAY, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MILLISECOND, 0);
          startDate = cal.getTime();
          break;
        case 7:
          cal.add(Calendar.DAY_OF_MONTH, -6);
          cal.set(Calendar.HOUR_OF_DAY, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MILLISECOND, 0);
          startDate = cal.getTime();
          break;
        case 30:
          cal.add(Calendar.DAY_OF_MONTH, -29);
          cal.set(Calendar.HOUR_OF_DAY, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MILLISECOND, 0);
          startDate = cal.getTime();
          break;
        case 365:
          cal.add(Calendar.YEAR, -1);
          cal.set(Calendar.HOUR_OF_DAY, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MILLISECOND, 0);
          startDate = cal.getTime();
          break;
        default:
          throw new IllegalArgumentException("Invalid days parameter");
      }

      List<BookingByTypeRes> results = _ChartRepository.countBookingGroupByType(startDate, endDate);

      return new CustomResult(200, "ok", results);
    } catch (Exception e) {
      e.printStackTrace();
      return new CustomResult(400, "error", e.getMessage());
    }
  }

}
