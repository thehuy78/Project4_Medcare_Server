package projectsem4.medcare_server.interfaces.admin;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IPack {
  CustomResult GetByHospital(FilterRes res);

  CustomResult GetById(Long id);

  CustomResult Create(PackageDto packageDto);

  CustomResult Update(PackageDto packageDto);

  CustomResult ChangeStatus(Long id);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class PackageDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Double fee;
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
  public class PackRes {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Double fee;
    private Date createDate;
    private Date updateDate;
    private String status;
    private String hospitalName;
    private String hospitalCode;
    private String emailUser;
  }
}
