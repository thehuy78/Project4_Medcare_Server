package projectsem4.medcare_server.interfaces.admin;

import org.springframework.web.multipart.MultipartFile;
import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface IHospital {
  CustomResult GetAll(FilterRes res);

  CustomResult Create(HospitalDto h);

  CustomResult Update(HospitalUpdateDto h);

  CustomResult GetById(Long id);

  CustomResult GetNameAndCode();

  CustomResult ChangeStatusHospital(Long id);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class HospitalDto {
    private Long id;
    private String name;
    private String code;
    private Long type;
    private String map;
    private String province;
    private String district;
    private String address;
    private String openTime;
    private String closeTime;
    private String workDay;
    private String description;
    private MultipartFile photoFile;
    private List<MultipartFile> photoImage;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class HospitalUpdateDto {
    private Long id;
    private String name;
    private String code;
    private Long type;
    private String province;
    private String district;
    private String address;
    private String openTime;
    private String closeTime;
    private String workDay;
    private String logoOld;
    private String imgOld;
    private String description;
    private MultipartFile photoFile;
    private List<MultipartFile> photoImage;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class HospitalRes {
    private Long id;
    private String code;
    private String name;
    private String logo;
    private String image;
    private String district;
    private String province;
    private String openTime;
    private String closeTime;
    private String workDay;
    private String address;
    private String map;
    private String description;
    private Date createDate;
    private Date updateDate;
    private String status;
    private String typeName;

  }

  // @Data
  // @AllArgsConstructor
  // @NoArgsConstructor
  // public class ServiceRes {
  // private Integer id;
  // private String name;
  // }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class FilterRes {
    private Integer page;
    private Integer size;
    private String type;
    private String status;
    private String address;
    private String search;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class HospitalResNameAndCode {
    private String code;
    private String name;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class HospitalResNameAndCodeAndId {
    private Long id;
    private String code;
    private String name;
  }

}
