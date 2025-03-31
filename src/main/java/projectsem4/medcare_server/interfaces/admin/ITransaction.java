package projectsem4.medcare_server.interfaces.admin;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface ITransaction {
  CustomResult GetAll(Filter res);

  CustomResult ExportFile(Filter res);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class Filter {
    private String type;
    private Date startDate;
    private Date endDate;
    private Integer page;
    private Integer size;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public class TransactionRes {
    private Long id;
    private Double traded;
    private Double balance;
    private String description;
    private Date createDate;
    private Date updateDate;
    private String status;

    private String email;
  }

}
