package projectsem4.medcare_server.interfaces.admin;

import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.dto.admin.LoginRes;

public interface IUser {

  CustomResult Login(LoginRes res);

  CustomResult LoginAdmin(LoginRes res);

  CustomResult Register(RegisterDto res);

  CustomResult Forgot(String email);

  CustomResult UpdatePassword(UpdatePasswordDto dto);

  CustomResult ResetPassword(String code, String email);

  CustomResult Logout(String token);

  CustomResult GetAccount(FilterRes res);

  CustomResult Sendmail(MailRes res);

  CustomResult ChangeStatusUser(Long id);

  CustomResult UpdateInformation(UpdateInformation dto);

  // login qr
  CustomResult RequestQr(String key, String email);

  CustomResult GetUsertQr(String email, String key, String location);

  CustomResult LoginQr(String key, String token);

  CustomResult RefeshToken(String token);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class UpdateInformation {
    private String firstName;
    private String lastName;
    private String phone;
    private MultipartFile avatar;
    private Long id;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class InforLoginQrRes {
    private String firstName;
    private String lastName;
    private String avatar;
    private String email;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class FilterRes {
    private String status;
    private String search;
    private String role;
    private Integer page;
    private Integer size;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class StompClientDto {
    private String firstName;
    private String lastName;
    private String avatar;
    private String hospital;
    private String email;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class UpdatePasswordDto {
    private Long id;
    private String passwordOld;
    private String passwordNew;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class MailRes {
    private String emailTo;
    private String subject;
    private String message;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class UserCustomerRes {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
    private String role;
    private String verify;
    private Double balance;
    private Date createDate;
    private Date updateDate;
    private String status;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class UserAdminRes {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
    private String role;
    private String verify;

    private Date createDate;
    private Date updateDate;
    private String status;
    private String hospitalName;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class RegisterDto {
    private String firstName;
    private String lastName;
    private String role;
    private String email;
    private String password;
    private String hospitalCode;
    private MultipartFile avatar;
  }

}
