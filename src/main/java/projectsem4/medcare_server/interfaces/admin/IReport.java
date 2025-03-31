package projectsem4.medcare_server.interfaces.admin;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IReport {
  CustomResult ExportFile(FilterRes filter);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class FilterRes {
    private Integer page;
    private Integer size;
    private String search;
    private String gender;
    private List<Double> revenue;
    private String type;
    private String typeUrl;
    private String idUrl;
    private String hospitalCode;
    private Date startDate; // New field for start date
    private Date endDate; // New field for end date
  }
}
