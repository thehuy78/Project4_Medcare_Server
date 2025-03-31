package projectsem4.medcare_server.interfaces.admin;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface INews {
  CustomResult GetAll(String search, Integer page, Integer size);

  CustomResult FindById(Long id);

  CustomResult Create(NewsDto dto);

  CustomResult Update(NewsDto dto);

  CustomResult ChangeStatus(Long id);

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class NewsRes {
    private Long id;
    private String title;
    private String image;
    private String description;
    private Date createDate;
    private Date updateDate;
    private String status;
    private String avatarUser;
    private String firstName;
    private String lastName;
    private Long idUser;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class NewsDto {
    private Long id;
    private String title;
    private String image;
    private MultipartFile imageFile;
    private String description;
    private Long idUser;
  }

}
