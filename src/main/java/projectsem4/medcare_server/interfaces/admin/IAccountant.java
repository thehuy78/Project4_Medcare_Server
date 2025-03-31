package projectsem4.medcare_server.interfaces.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IAccountant {

  CustomResult GetWeekAccountant(String hospitalId);

  CustomResult GetMonthAccountant(String hospitalId);

  CustomResult ExportFile(FilterAccountant hospitalId);

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class FilterAccountant {
    private String hospitalCode;
    private Integer time;
  }

}
