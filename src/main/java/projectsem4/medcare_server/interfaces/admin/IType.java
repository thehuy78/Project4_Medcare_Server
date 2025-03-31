package projectsem4.medcare_server.interfaces.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IType {
  CustomResult GetTypeBooking();

  CustomResult GetTypeHospital();

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class TypeDto {
    private Long id;
    private String name;
  }
}
