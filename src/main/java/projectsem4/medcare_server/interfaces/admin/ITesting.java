package projectsem4.medcare_server.interfaces.admin;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface ITesting {
  CustomResult GetByHospital(FilterRes res);

  CustomResult GetById(Long id);

  CustomResult Create(TestingDto dto);

  CustomResult Update(TestingDto dto);

  CustomResult ChangeStatus(Long id);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class TestingDto {
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
  public class TestingRes {
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
