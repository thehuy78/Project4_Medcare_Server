package projectsem4.medcare_server.interfaces.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IVaccine {
  CustomResult GetByHospital(FilterRes res);

  CustomResult GetById(Long id);

  CustomResult Create(VaccineDto dto);

  CustomResult Update(VaccineDto dto);

  CustomResult ChangeStatus(Long id);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class VaccineDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Double fee;
    private Integer floor;
    private String zone;
    private String status;
    private Long hospitalId;
    private Long userId;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class FilterRes {
    private Integer page;
    private Integer size;
    private String status;
    private String search;
    private List<Double> fee;
    private String codehospital;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class VaccineRes {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Double fee;
    private Integer floor;

    private String zone;
    private Date createDate;
    private Date updateDate;
    private String status;
    private String hospitalName;
    private String hospitalCode;
    private String emailUser;
  }
}
