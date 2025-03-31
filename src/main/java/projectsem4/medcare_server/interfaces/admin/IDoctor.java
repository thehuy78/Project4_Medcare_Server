package projectsem4.medcare_server.interfaces.admin;

import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IDoctor {
  CustomResult GetAll(FilterRes res);

  CustomResult GetByHospital(Long idhospital);

  CustomResult GetById(Long id);

  CustomResult Create(DoctorDto dto);

  CustomResult Update(DoctorDto dto);

  CustomResult ChangeStatus(Long id);

  CustomResult TopDoctor(DoctorTop top);

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class DoctorTop {
    private Long hospitalId;
    private Integer day;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class DoctorDto {
    private Long id;
    private String code;
    private String name;
    private String workDay;
    private String timeWork;
    private String level;
    private String gender;
    private Double fee;
    private Integer room;
    private Integer patients;
    private MultipartFile avatar;
    private String status;
    private Long departmentId;
    private Long hospitalId;
    private Long userId;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class FilterRes {
    private Integer page;
    private Integer size;
    private String search;
    private String codehospital;
    private String level;
    private String timeWork;
    private String gender;
    private List<Integer> fee;
    private String codedepartment;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class DoctorRes {
    private Long id;
    private String code;
    private String name;
    private String workDay;
    private String timeWork;
    private String level;
    private String gender;
    private Double fee;
    private Integer room;
    private Integer patients;
    private String avatar;
    private Date createDate;
    private Date updateDate;
    private String status;
    private String hospitalCode;
    private String departmentCode;
    private String hospitalName;
    private String departmentName;
    private Long departmentId;
    private Long userId;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class DoctorTopRes {
    private Long id;
    private String code;
    private String name;
    private String level;
    private String avatar;
    private Long countBooking;
  }
}
