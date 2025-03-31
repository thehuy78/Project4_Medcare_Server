package projectsem4.medcare_server.controller.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.dto.admin.LoginRes;

import projectsem4.medcare_server.interfaces.admin.IUser;
import projectsem4.medcare_server.interfaces.admin.IUser.FilterRes;
import projectsem4.medcare_server.interfaces.admin.IUser.MailRes;
import projectsem4.medcare_server.interfaces.admin.IUser.RegisterDto;
import projectsem4.medcare_server.interfaces.admin.IUser.UpdateInformation;
import projectsem4.medcare_server.interfaces.admin.IUser.UpdatePasswordDto;
import projectsem4.medcare_server.security.InactiveTokenStore;
import projectsem4.medcare_server.security.JwtUtil;
import projectsem4.medcare_server.service.admin.UserService;

@RestController
@RequestMapping("/api/admin/auth")
public class UserController {

  @Autowired
  UserService _user;

  @Autowired
  IUser iuser;

  @Autowired
  InactiveTokenStore inactiveTokenStore;

  @Autowired
  JwtUtil jwtUtil;

  @PostMapping("login")
  public CustomResult login(@RequestBody LoginRes r) {
    return iuser.Login(r);
  }

  @PostMapping("loginadmin")
  public CustomResult loginAdmin(@RequestBody LoginRes r) {
    return iuser.LoginAdmin(r);
  }

  @PostMapping("register")
  public CustomResult register(@ModelAttribute RegisterDto r) {
    return iuser.Register(r);
  }

  @PostMapping("resetpassword")
  public CustomResult resetPassword(@RequestBody Map<String, String> request) {
    String code = request.get("code");
    String email = request.get("email");
    return iuser.ResetPassword(code, email);
  }

  @PostMapping("updatePassword")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult updatePassword(@RequestBody UpdatePasswordDto dto) {
    return iuser.UpdatePassword(dto);
  }

  @PostMapping("forgot")
  public CustomResult forgot(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    return iuser.Forgot(email);
  }

  @PostMapping("logout")
  public CustomResult logout(@RequestBody String token) {
    return iuser.Logout(token);
  }

  @PostMapping("getaccount")
  @RolesAllowed({ "samedcare" })
  public CustomResult GetAccount(@RequestBody FilterRes res) {
    return iuser.GetAccount(res);
  }

  @GetMapping("changestatus/{id}")
  @RolesAllowed({ "samedcare" })
  public CustomResult changeStatusUser(@PathVariable Long id) {
    return iuser.ChangeStatusUser(id);
  }

  @PostMapping("sendMail")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult SendMailUser(@RequestBody MailRes res) {
    return iuser.Sendmail(res);
  }

  @PostMapping("updateinfor")
  @RolesAllowed({ "admin" })
  public CustomResult UpdateInformation(@ModelAttribute UpdateInformation dto) {
    return iuser.UpdateInformation(dto);
  }

  // LOGIN WITH QR MOBILE
  // QUÉT MOBILE QR GỬI EMAIL CHO WEBSITE
  @GetMapping("requestqr/{key}/{email}")
  public CustomResult requestQr(@PathVariable String key, @PathVariable String email) {
    return iuser.RequestQr(key, email);
  }

  // WEB NHẬN REALTIME VÀ FETCH USER RENDER ĐỒNG THỜI GỬI REALTIME MOBILE ACCEPT
  // LOGIN
  @GetMapping("getuserqr/{email}/{key}/{location}")
  public CustomResult GetUserQr(@PathVariable String email, @PathVariable String key, @PathVariable String location) {
    return iuser.GetUsertQr(email, key, location);
  }

  @PostMapping("loginqr")
  public CustomResult LoginQR(@RequestBody Map<String, String> request) {
    String code = request.get("key");
    String token = request.get("token");
    return iuser.LoginQr(code, token);
  }

  @PostMapping("refeshToken")
  public CustomResult RefeshToken(@RequestBody Map<String, String> request) {
    String token = request.get("token");
    return iuser.RefeshToken(token);
  }
}
