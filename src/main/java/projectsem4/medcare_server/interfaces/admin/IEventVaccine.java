package projectsem4.medcare_server.interfaces.admin;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IEventVaccine {
  CustomResult GetAll(Filter res);

  CustomResult Create(EventVaccineDto dto);

  CustomResult Update(EventVaccineDto dto);

  CustomResult ChangeStatus(Long id);

  CustomResult GetById(Long id);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class Filter {
    private Integer page;
    private Integer size;
    private String search;
    private String status;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class EventVaccineRes {
    private Long id;
    private String code;
    private String name;
    private Date createDate;
    private Date updateDate;
    private String status;
    private String description;
    private String emailUser;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class EventVaccineDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Long userId;
  }
}
