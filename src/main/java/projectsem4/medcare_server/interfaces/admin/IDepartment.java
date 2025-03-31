package projectsem4.medcare_server.interfaces.admin;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IDepartment {
  CustomResult GetAll(FilterRes res);

  CustomResult Create(DepartmentDto dto);

  CustomResult Update(DepartmentDto dto);

  CustomResult ChangeStatus(Long id);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class FilterRes {
    private Integer page;
    private Integer size;
    private String status;
    private String search;
    private String codehospital;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class DepartmentDto {
    private Long id;
    private String code;
    private String name;
    private Integer floor;
    private String zone;
    private Long hospitalId;
    private Long userId;
    private String status;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class DepartmentRes {
    private Long id;
    private String code;
    private String name;
    private Integer floor;
    private String zone;
    private Date createDate;
    private Date updateDate;
    private String status;
    private String nameHospital;
    private String codeHospital;
    private Long userId;
    private Long doctorCount;
  }
}
