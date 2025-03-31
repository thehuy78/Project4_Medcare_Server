package projectsem4.medcare_server.interfaces.admin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import org.springframework.core.io.ByteArrayResource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IBooking {
  CustomResult GetAll(FilterRes res);

  CustomResult GetBookingByType(FilteAdminHospital res);

  CustomResult GetById(Long id);

  CustomResult GetServiceBooking(Long id);

  CustomResult GetExcel(FilterExcel res);

  CustomResult SearchBooking(SearchBookingByHospital filter);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class FilteAdminHospital {
    private Integer page;
    private Integer size;
    private String search;
    private String type;
    private String hospitalCode;
    private Date startDate; // New field for start date
    private Date endDate; // New field for end date
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class BookingDetailsRes {
    private Long id;
    private String name;
    private String gender;
    private Date dob;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String identifier;
    private String job;
    private String hospitalName;
    private String hospitalCode;
    private String doctorLevel;
    private String doctorName;
    private String doctorCode;
    private String packName;
    private String packCode;
    private String testName;
    private String testCode;
    private String vaccineName;
    private String vaccineCode;
    private String hospitalLogo;
    private Date scheduleDate;
    private String scheduleTime;
    private String status;
    private Date createDate;
    private String email;
    private String firstName;
    private String lastName;
    private String verify;
    private Double revenue;
    private Double profit;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class SearchBookingByHospital {
    private Integer page;
    private Integer size;
    private String search;
    private String hospitalCode;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class FilterRes {
    private Integer page;
    private Integer size;
    private String search;
    private String gender;
    private List<Double> revenue;
    private String type;
    private String typeUrl;
    private String idUrl;
    private String hospitalCode;
    private Date startDate; // New field for start date
    private Date endDate; // New field for end date
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class FilterExcel {
    private String type;
    private Date startDate;
    private Date endDate;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class BookingRes {
    private Long id;
    private Date bookingDate;
    private String bookingTime;
    private String description;
    private Double revenue;
    private Double profit;
    private Date createDate;
    private Date updateDate;
    private String status;
    private String hospitalName;
    private String typeName;
    private String userEmail;
    private String name;
    private String phone;
    private String gender;
    private Date dob;
    private String province;
    private String identifier;
  }

}
