package projectsem4.medcare_server.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

import projectsem4.medcare_server.controller.admin.HospitalEventController;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.EventVaccine;
import projectsem4.medcare_server.domain.entity.HospitalEvent;
import projectsem4.medcare_server.interfaces.admin.IAnalysis;
import projectsem4.medcare_server.repository.admin.AnalysisRepository;
import projectsem4.medcare_server.repository.admin.EventVaccineRepository;
import projectsem4.medcare_server.repository.admin.HospitalEventRepository;

@Service
public class AnalysisService implements IAnalysis {

  @Autowired
  AnalysisRepository _analysisRepo;

  @Autowired
  HospitalEventRepository _HospitalEventRepo;

  @Autowired
  EventVaccineRepository _EventVaccineRepo;

  @Override
  public CustomResult Chart1(Chart1Dto chartDto) {
    try {
      Date currentDate = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(currentDate);

      // Xác định ngày bắt đầu và kết thúc cho khoảng thời gian hiện tại và trước

      Date startDate, endDate, prevStartDate, prevEndDate;
      int time = chartDto.getTime();

      switch (time) {
        case 1: // Daily - hôm nay và hôm qua

          setToEndOfDay(cal);
          endDate = cal.getTime();
          setToStartOfDay(cal);
          startDate = cal.getTime();
          setToEndOfDay(cal);
          cal.add(Calendar.DATE, -1);
          prevEndDate = cal.getTime();
          setToStartOfDay(cal);
          prevStartDate = cal.getTime();
          break;

        case 7: // Weekly - tuần này và tuần trước

          setToEndOfDay(cal);
          endDate = cal.getTime();
          cal.add(Calendar.DATE, -6);
          setToStartOfDay(cal);
          startDate = cal.getTime();

          setToEndOfDay(cal);
          cal.add(Calendar.DATE, -1);
          prevEndDate = cal.getTime();
          setToStartOfDay(cal);
          cal.add(Calendar.DATE, -6);
          prevStartDate = cal.getTime();
          break;

        case 30: // Monthly - tháng này và tháng trước
          setToEndOfDay(cal);
          endDate = cal.getTime();
          cal.add(Calendar.DATE, -29);
          setToStartOfDay(cal);
          startDate = cal.getTime();
          setToEndOfDay(cal);
          cal.add(Calendar.DATE, -1);
          prevEndDate = cal.getTime();
          cal.add(Calendar.DATE, -29);
          setToStartOfDay(cal);
          prevStartDate = cal.getTime();
          break;

        case 365: // Yearly - năm nay và năm trước
          cal.set(Calendar.DAY_OF_YEAR, 1); // Đầu năm
          startDate = cal.getTime();
          cal.add(Calendar.YEAR, -1);
          prevStartDate = cal.getTime();

          cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
          prevEndDate = cal.getTime();
          cal.add(Calendar.YEAR, 1);
          endDate = cal.getTime();
          break;

        default:
          return new CustomResult(400, "Invalid time parameter", null);
      }

      // Fetch dữ liệu booking cho khoảng thời gian hiện tại và trước đó
      List<Object[]> currentPeriodData = _analysisRepo.fetchBookingData(
          chartDto.getHospitalCode(),
          chartDto.getType(),
          chartDto.getService(),
          chartDto.getTime(),
          startDate,
          endDate);

      List<Object[]> previousPeriodData = _analysisRepo.fetchBookingData(
          chartDto.getHospitalCode(),
          chartDto.getType(),
          chartDto.getService(),
          chartDto.getTime(),
          prevStartDate,
          prevEndDate);

      // Xử lý kết quả trả về thành các danh sách có định dạng yêu cầu
      List<ChartData> currentPeriodList = transformData(currentPeriodData, time);
      List<ChartData> previousPeriodList = transformData(previousPeriodData, time);

      // Tạo map kết quả với hai danh sách
      Map<String, List<ChartData>> resultMap = new HashMap<>();
      resultMap.put("currentPeriod", currentPeriodList);
      resultMap.put("previousPeriod", previousPeriodList);

      return new CustomResult(200, "ok", resultMap);

    } catch (Exception e) {
      return new CustomResult(400, "Error generating chart data", e.getMessage());
    }
  }

  // ss
  // Hàm helper để chuẩn hóa thời gian bắt đầu của ngày
  private void setToStartOfDay(Calendar cal) {
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
  }

  private void setToEndOfDay(Calendar cal) {
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 999);
  }

  // Chuyển đổi dữ liệu kết quả từ List<Object[]> sang List<ChartData>
  private List<ChartData> transformData(List<Object[]> data, int time) {
    List<ChartData> chartDataList = new ArrayList<>();

    // Tạo một Map để lưu trữ các giá trị trả về theo period
    Map<Integer, ChartData> dataMap = new HashMap<>();

    // Chuyển đổi dữ liệu vào Map
    for (Object[] row : data) {
      Integer period = (Integer) row[0];
      Long bookingsCount = ((Number) row[1]).longValue();
      Long totalRevenue = ((Number) row[2]).longValue();
      Long totalProfit = ((Number) row[3]).longValue();

      dataMap.put(period, new ChartData(period, bookingsCount, totalRevenue, totalProfit));
    }

    // Đảm bảo đủ số lượng đối tượng cho từng khoảng thời gian
    switch (time) {
      case 1: // 24 giờ
        for (int i = 0; i < 24; i++) {
          ChartData dataPoint = dataMap.getOrDefault(i, new ChartData(i, 0L, 0L, 0L));
          chartDataList.add(dataPoint);
        }
        break;

      case 7: // 7 ngày
        for (int i = 1; i <= 7; i++) {
          ChartData dataPoint = dataMap.getOrDefault(i, new ChartData(i, 0L, 0L, 0L));
          chartDataList.add(dataPoint);
        }
        break;

      case 30: // 7 ngày trong tuần
        for (int i = 1; i <= 7; i++) {
          ChartData dataPoint = dataMap.getOrDefault(i, new ChartData(i, 0L, 0L, 0L));
          chartDataList.add(dataPoint);
        }
        break;

      case 365: // 12 tháng
        for (int i = 1; i <= 12; i++) {
          ChartData dataPoint = dataMap.getOrDefault(i, new ChartData(i, 0L, 0L, 0L));
          chartDataList.add(dataPoint);
        }
        break;

      default:
        throw new IllegalArgumentException("Invalid time parameter");
    }

    return chartDataList;
  }

  @Override
  public CustomResult ChartCompare(ChartCompareDto chartDto) {
    try {
      Date currentDate = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(currentDate);

      // Xác định ngày bắt đầu và kết thúc cho khoảng thời gian hiện tại và trước

      Date startDate, endDate;
      int time = chartDto.getTime();

      switch (time) {
        case 1: // Daily - hôm nay và hôm qua

          setToEndOfDay(cal);
          endDate = cal.getTime();
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 7: // Weekly - tuần này và tuần trước

          setToEndOfDay(cal);
          endDate = cal.getTime();
          cal.add(Calendar.DATE, -6);
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 30: // Monthly - tháng này và tháng trước
          setToEndOfDay(cal);
          endDate = cal.getTime();
          cal.add(Calendar.DATE, -29);
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 365: // Yearly - năm nay và năm trước
          cal.set(Calendar.DAY_OF_YEAR, 1); // Đầu năm
          startDate = cal.getTime();
          cal.add(Calendar.YEAR, 1);
          endDate = cal.getTime();
          break;

        default:
          return new CustomResult(400, "Invalid time parameter", null);
      }

      // Fetch dữ liệu booking cho khoảng thời gian hiện tại và trước đó
      List<Object[]> currentPeriodData = _analysisRepo.fetchBookingData(
          chartDto.getHospitalFirstCode(),
          chartDto.getType(),
          chartDto.getService(),
          chartDto.getTime(),
          startDate,
          endDate);

      List<Object[]> previousPeriodData = _analysisRepo.fetchBookingData(
          chartDto.getHospitalSecondCode(),
          chartDto.getType(),
          chartDto.getService(),
          chartDto.getTime(),
          startDate,
          endDate);

      // Xử lý kết quả trả về thành các danh sách có định dạng yêu cầu
      List<ChartData> currentPeriodList = transformData(currentPeriodData, time);
      List<ChartData> previousPeriodList = transformData(previousPeriodData, time);

      // Tạo map kết quả với hai danh sách
      Map<String, List<ChartData>> resultMap = new HashMap<>();
      resultMap.put("currentPeriod", currentPeriodList);
      resultMap.put("previousPeriod", previousPeriodList);

      return new CustomResult(200, "ok", resultMap);

    } catch (Exception e) {
      return new CustomResult(400, "Error generating chart data", e.getMessage());
    }
  }

  @Override
  public CustomResult AnalytisAllServiceHospital(Chart1Dto chartDto) {
    try {
      Date currentDate = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(currentDate);

      // Xác định ngày bắt đầu và kết thúc cho khoảng thời gian hiện tại và trước

      Date startDate, endDate;
      int time = chartDto.getTime();

      switch (time) {
        case 1: // Daily - hôm nay và hôm qua

          setToEndOfDay(cal);
          endDate = cal.getTime();
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 7: // Weekly - tuần này và tuần trước

          setToEndOfDay(cal);
          endDate = cal.getTime();
          cal.add(Calendar.DATE, -6);
          setToStartOfDay(cal);
          startDate = cal.getTime();
          break;

        case 30: // Monthly - tháng này và tháng trước
          setToEndOfDay(cal);
          endDate = cal.getTime();
          cal.add(Calendar.DATE, -29);
          setToStartOfDay(cal);
          startDate = cal.getTime();
          break;

        case 365: // Yearly - năm nay và năm trước
          cal.set(Calendar.DAY_OF_YEAR, 1); // Đầu năm
          startDate = cal.getTime();
          cal.add(Calendar.YEAR, 1);
          endDate = cal.getTime();
          break;

        default:
          return new CustomResult(400, "Invalid time parameter", null);
      }

      // Fetch dữ liệu booking cho khoảng thời gian hiện tại và trước đó
      List<ChartDataService> currentPeriodData = _analysisRepo.fetchBookingHospital(
          chartDto.getHospitalCode(),
          chartDto.getType(),
          startDate,
          endDate);

      return new CustomResult(200, "ok", currentPeriodData);

    } catch (Exception e) {
      return new CustomResult(400, "Error generating chart data", e.getMessage());
    }
  }

  @Override
  public CustomResult ChartMapVN(ChartMapDto chartMapDto) {
    try {
      Date currentDate = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(currentDate);

      // Xác định ngày bắt đầu và kết thúc cho khoảng thời gian hiện tại và trước

      Date startDate, endDate;
      int time = chartMapDto.getTime();

      switch (time) {
        case 1: // Daily - hôm nay và hôm qua

          setToEndOfDay(cal);
          endDate = cal.getTime();
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 7: // Weekly - tuần này và tuần trước

          setToEndOfDay(cal);
          endDate = cal.getTime();
          cal.add(Calendar.DATE, -6);
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 30: // Monthly - tháng này và tháng trước
          setToEndOfDay(cal);
          endDate = cal.getTime();
          cal.add(Calendar.DATE, -29);
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 365: // Yearly - năm nay và năm trước
          cal.set(Calendar.DAY_OF_YEAR, 1); // Đầu năm
          startDate = cal.getTime();
          cal.add(Calendar.YEAR, 1);
          endDate = cal.getTime();
          break;

        default:
          return new CustomResult(400, "Invalid time parameter", null);
      }

      // Fetch dữ liệu booking cho khoảng thời gian hiện tại và trước đó
      List<ChartMapVN> data = _analysisRepo.countChartMap(
          chartMapDto.getVaccineCode(),
          startDate,
          endDate);

      Map<String, Long> provinceCountMap = data.stream()
          .collect(Collectors.toMap(ChartMapVN::getProvince, ChartMapVN::getCount));

      // Tạo danh sách kết quả đầy đủ 63 tỉnh thành
      List<ChartMapVN> fullResult = PROVINCES.stream()
          .map(province -> {
            Long count = provinceCountMap.getOrDefault(province, 0L);
            return new ChartMapVN(province, count);
          })
          .collect(Collectors.toList());

      long totalCount = fullResult.stream()
          .mapToLong(ChartMapVN::getCount)
          .sum();

      // Tính toán tỷ lệ phần trăm
      List<Map<String, Object>> resultMap = fullResult.stream()
          .map(provinceData -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", provinceData.getProvince());
            map.put("count", provinceData.getCount());
            map.put("percent", totalCount > 0
                ? (double) provinceData.getCount() / totalCount
                : 0.0);
            return map;
          })
          .collect(Collectors.toList());

      return new CustomResult(200, "ok", resultMap);

      // return new CustomResult(200, "ok", fullResult);

    } catch (Exception e) {
      return new CustomResult(400, "Error generating chart data", e.getMessage());
    }
  }

  public static final List<String> PROVINCES = Arrays.asList(
      "Hà Nội", "Hồ Chí Minh city", "Đà Nẵng", "Phú Yên", "Bình Định", "Hải Phòng",
      "Cần Thơ", "Hải Dương", "Bắc Giang", "Bắc Ninh", "Quảng Ninh", "Đắk Lắk",
      "Lâm Đồng", "Thừa Thiên - Huế", "Long An", "An Giang", "Hậu Giang", "Tiền Giang",
      "Sóc Trăng", "Cà Mau", "Kiên Giang", "Trà Vinh", "Vĩnh Long", "Đồng Tháp",
      "Bến Tre", "Nghệ An", "Hà Tĩnh", "Quảng Bình", "Quảng Trị", "Gia Lai", "Kon Tum",
      "Thanh Hóa", "Lào Cai", "Sơn La", "Tuyên Quang", "Cao Bằng", "Lai Châu", "Yên Bái",
      "Hòa Bình", "Bắc Kạn", "Vĩnh Phúc", "Phú Thọ", "Ninh Bình", "Quảng Nam", "Hà Giang",
      "Thái Nguyên", "Nam Định", "Lạng Sơn", "Tây Ninh", "Bình Thuận", "Bà Rịa - Vũng Tàu",
      "Ninh Thuận", "Khánh Hòa", "Đăk Nông", "Bình Phước", "Đồng Nai", "Bình Dương",
      "Bạc Liêu", "Quảng Ngãi", "Thái Bình", "Hà Nam", "Hưng Yên", "Điện Biên");

  @Override
  public CustomResult ChartMapVNEvent(ChartMapEvent chartMapDto) {
    try {
      Date currentDate = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(currentDate);

      // Xác định ngày bắt đầu và kết thúc cho khoảng thời gian hiện tại và trước

      Date startDate, endDate;
      int time = chartMapDto.getTime();

      switch (time) {
        case 1: // Daily - hôm nay và hôm qua

          setToEndOfDay(cal);
          endDate = cal.getTime();
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 7: // Weekly - tuần này và tuần trước

          setToEndOfDay(cal);
          endDate = cal.getTime();
          cal.add(Calendar.DATE, -6);
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 30: // Monthly - tháng này và tháng trước
          setToEndOfDay(cal);
          endDate = cal.getTime();
          cal.add(Calendar.DATE, -29);
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 365: // Yearly - năm nay và năm trước
          cal.set(Calendar.DAY_OF_YEAR, 1); // Đầu năm
          startDate = cal.getTime();
          cal.add(Calendar.YEAR, 1);
          endDate = cal.getTime();
          break;

        default:
          return new CustomResult(400, "Invalid time parameter", null);
      }
      var isEvent = _EventVaccineRepo.findById(chartMapDto.getEventId());
      if (!isEvent.isPresent()) {
        return new CustomResult(400, "Event is not Exist", null);
      }
      EventVaccine event = isEvent.get();

      List<HospitalEvent> hospitalEvent = _HospitalEventRepo.findByEventVaccine(event);
      List<Long> idVaccines = new ArrayList<Long>();
      if (hospitalEvent.size() > 0) {
        hospitalEvent.forEach(t -> idVaccines.add(t.getVaccine().getId()));
      }

      List<Long> ids = (idVaccines == null || idVaccines.isEmpty()) ? null : idVaccines;

      List<ChartMapVN> data = new ArrayList<ChartMapVN>();
      if (ids == null) {

      } else {
        data = _analysisRepo.countChartMapEvent(
            ids,
            startDate,
            endDate);
      }
      // Fetch dữ liệu booking cho khoảng thời gian hiện tại và trước đó

      Map<String, Long> provinceCountMap = data.stream()
          .collect(Collectors.toMap(ChartMapVN::getProvince, ChartMapVN::getCount));

      // Tạo danh sách kết quả đầy đủ 63 tỉnh thành
      List<ChartMapVN> fullResult = PROVINCES.stream()
          .map(province -> {
            Long count = provinceCountMap.getOrDefault(province, 0L);
            return new ChartMapVN(province, count);
          })
          .collect(Collectors.toList());

      long totalCount = fullResult.stream()
          .mapToLong(ChartMapVN::getCount)
          .sum();

      // Tính toán tỷ lệ phần trăm
      List<Map<String, Object>> resultMap = fullResult.stream()
          .map(provinceData -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", provinceData.getProvince());
            map.put("count", provinceData.getCount());
            map.put("percent", totalCount > 0
                ? (double) provinceData.getCount() / totalCount
                : 0.0);
            return map;
          })
          .collect(Collectors.toList());

      return new CustomResult(200, "ok", resultMap);

      // return new CustomResult(200, "ok", fullResult);

    } catch (Exception e) {
      return new CustomResult(400, "Error generating chart data", e.getMessage());
    }
  }

}
