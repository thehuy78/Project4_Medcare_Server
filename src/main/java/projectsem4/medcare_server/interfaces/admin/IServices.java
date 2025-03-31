package projectsem4.medcare_server.interfaces.admin;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IServices {
  CustomResult GetHospitalService(Integer pageNumber, Integer pageSize, String search);

  CustomResult GetService();

  CustomResult GetServiceByHospital(String hospitalCode);

  CustomResult ChangeStatusService(Long id);

  CustomResult AddService(String name);

  CustomResult EditService(AttributeRes res);

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class ServicesRes {
    private Long id;
    private Long idHospital;
    private Long idAttribute;
    private String status;
    private String name;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class HospitalWithServices {
    private Long id;
    private String code;
    private String name;
    private List<ServicesRes> services;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class AttributeRes {
    private Long id;
    private String name;

  }

}
