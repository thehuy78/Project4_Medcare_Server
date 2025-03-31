package projectsem4.medcare_server.interfaces.admin;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IOffline {
  CustomResult Create(OfflineDto dto);

  CustomResult GetByHospital(OfflineFilter filter);

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class OfflineDto {
    private List<Long> idDoctor;
    private String day;
    private String description;
    private String status;
    private Long userId;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class OfflineFilter {
    private Integer page;
    private Integer size;
    private String search;
    private String status;
    private Long idHospital;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class OfflineRes {
    private Long id;
    private String day;
    private String description;
    private String status;
    private String user;
    private String doctorCode;
    private String doctorName;
    private String avatar;
    private Date createDate;
    private Date updateDate;
  }
}
