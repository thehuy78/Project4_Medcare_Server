package projectsem4.medcare_server.interfaces.admin;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IHospitalEvent {
  CustomResult GetByEvent(Filter filter);

  CustomResult AddHospitalEvent(AddDto dto);

  CustomResult RemoveHospitalEvent(Long id);

  CustomResult ChangeStatus(Long id);

  CustomResult Denied(Long id);

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class Filter {
    private Long idEvent;
    private Long hospitalId;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class AddDto {
    private Long idEvent;
    private Long idVaccine;
    private Long idUser;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class HospitalEventRes {
    private Long id;
    private Date createDate;
    private Date updateDate;
    private String status;
    private Long hospitalId;
    private String hospitalName;
    private String hospitalCode;
    private Long vaccineId;
    private String vaccineName;
    private String vaccineCode;
    private Double vaccineFee;
    private String vaccineStatus;

  }
}
