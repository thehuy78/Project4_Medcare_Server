package projectsem4.medcare_server.interfaces.admin;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface INotification {
  CustomResult GetAll(filterRes res);

  CustomResult CreateByAdmin(createNoti create);

  CustomResult DeletedById(Long id);

  CustomResult DeletedMulti(List<Long> list);

  CustomResult DeletedAll(Long id);

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class filterRes {
    private Date startDate;
    private Date endDate;
    private Long idUser;
    private String type;
    private Integer page;
    private Integer size;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class createNoti {

    private String description;
    private String role;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class NotificationRes {
    private Long id;
    private String type;
    private String description;
    private Date createDate;
    private String status;

  }
}
