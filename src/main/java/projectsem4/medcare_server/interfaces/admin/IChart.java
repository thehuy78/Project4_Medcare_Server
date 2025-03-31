package projectsem4.medcare_server.interfaces.admin;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IChart {

  CustomResult TotalRevenue();

  CustomResult TotalRevenueHospital(String code);

  CustomResult TotalGenderBookingByDay(Integer day);

  CustomResult CountBookingByHours();

  CustomResult CountBookingByAge(Integer day);

  CustomResult countBookingsForWeek(Integer days);

  CustomResult countTopHospital(Integer day);

  CustomResult countBookingByType(Integer day);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class TotalRevenueRes {
    private Double totalRevenue;
    private Double totalProfit;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class TotalRevenueRespon {
    private Double totalRevenue;
    private Double totalProfit;
    private Double percentChangeRevenue;
    private Double percentChangeProfit;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class TotalGenderRes {
    private Long male;
    private Long female;
  }

  @Data
  @NoArgsConstructor
  public class TotalBookingByHoursRes {
    private Integer hour;
    private Long count;

    public TotalBookingByHoursRes(Integer hour, Long count) {
      this.hour = hour;
      this.count = count;
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class AgeGroupCount {
    private Long under18;
    private Long between18And50;
    private Long over50;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class BookingCountByDay {
    private String dayName;
    private Long bookingCount;
  }

  // @Data
  // @AllArgsConstructor
  // @NoArgsConstructor
  // public class TopHospitalBookingCount {
  // private String hospitalName;
  // private Long bookingCount;
  // }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class TopHospitalRespon {
    private Long id;
    private String code;
    private Long bookingCount;
    private Double revenueTotal;
    private Double profitTotal;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class BookingByTypeRes {
    private Long id;
    private String name;
    private Long count;
  }

}
