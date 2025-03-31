package projectsem4.medcare_server.interfaces.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IAnalysis {
  CustomResult Chart1(Chart1Dto chartDto);

  CustomResult ChartCompare(ChartCompareDto chartDto);

  CustomResult AnalytisAllServiceHospital(Chart1Dto chartDto);

  CustomResult ChartMapVN(ChartMapDto chartMapDto);

  CustomResult ChartMapVNEvent(ChartMapEvent chartMapEvent);

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class Chart1Dto {
    private String hospitalCode;
    private String type;
    private String service;
    private Integer time;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class ChartMapDto {
    private String vaccineCode;
    private Integer time;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class ChartMapEvent {
    private Long eventId;
    private Integer time;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class ChartCompareDto {
    private String hospitalFirstCode;
    private String hospitalSecondCode;
    private String type;
    private String service;
    private Integer time;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class ChartData {
    private Integer period;
    private Long bookingsCount;
    private Long totalRevenue;
    private Long totalProfit;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class ChartDataService {
    private String nameService;
    private Long bookingsCount;
    private Long totalRevenue;
    private Long totalProfit;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class ChartMapVN {
    private String province;
    private Long count;

  }

}
