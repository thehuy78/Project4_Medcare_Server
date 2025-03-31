package projectsem4.medcare_server.service.admin;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import lombok.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.transaction.Transactional;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.dto.admin.LoginRes;

import projectsem4.medcare_server.domain.entity.*;

import projectsem4.medcare_server.interfaces.admin.IUser;
import projectsem4.medcare_server.repository.admin.CodeVerifyRepository;
import projectsem4.medcare_server.repository.admin.HospitalRepository;
import projectsem4.medcare_server.repository.admin.UserDetailRepository;
import projectsem4.medcare_server.repository.admin.UserRepository;
import projectsem4.medcare_server.security.InactiveTokenStore;
import projectsem4.medcare_server.security.JwtUtil;
import projectsem4.medcare_server.service.EmailService;
import projectsem4.medcare_server.service.FirebaseRealtimeDatabaseService;
import projectsem4.medcare_server.service.FirebaseStorageService;

@Service
public class UserService implements IUser {

  @Autowired
  UserRepository userRepo;
  @Autowired
  UserDetailRepository userdetailRepo;

  @Autowired
  HospitalRepository hospRepo;
  @Autowired
  JwtUtil jwtUtil;

  @Autowired
  InactiveTokenStore inactiveTokenStore;

  @Autowired
  FirebaseStorageService firebaseStorageService;
  @Autowired
  EmailService _email;
  @Autowired
  BCryptPasswordEncoder passwordEncoder;

  @Autowired
  CodeVerifyRepository _codeVerifyRepo;

  @Autowired
  FirebaseRealtimeDatabaseService realtime;

  @Autowired
  RestTemplate restTemplate;

  public User findByEmail(String email) {
    try {
      // Attempt to find the user by email
      User u = userRepo.findByEmail(email);
      return u;
    } catch (Exception e) {
      return null;
    }
  }

  @Scheduled(fixedRate = 3600000) // 60 phút
  @Transactional
  public void deleteExpiredCodeVerifications() {
    Date now = new Date();
    int deletedRecords = _codeVerifyRepo.deleteByExpDateBefore(now);
    System.out.println("Deleted " + deletedRecords + " expired code verifications.");
  }

  @Scheduled(fixedRate = 10000) // 60 phút
  @Transactional
  public void check() {
    try {
      String url = "http://localhost:8082/stompclient/checked";
      Integer response = restTemplate.postForObject(url, null, Integer.class);
      long timestamp = System.currentTimeMillis(); // Lấy timestamp
      realtime.sendRealtimeMessageToUser("Status", "check " + timestamp);
    } catch (Exception e) {

    }
  }

  @Override
  public CustomResult Login(LoginRes res) {
    try {
      // Attempt to find the user by email
      User u = userRepo.findByEmail(res.getEmail());

      if (u != null) {
        if (u.getUserDetail().getRole().equalsIgnoreCase("samedcare")) {
          if (passwordEncoder.matches(res.getPassword(), u.getPassword())) {
            String token = jwtUtil.generateToken(u);
            long timestamp = System.currentTimeMillis(); // Lấy timestamp
            // realtime.sendRealtimeMessageToUser("Huy", "HeheLogin " + timestamp);
            // var html = "<p>Register Success !</p> <p>CODE VERIFY: <span style='color:
            // rgb(105, 153, 168);font-size:20px;' > 5678</span> </p>";
            // _email.sendEmail("thehuy0900@gmail.com", "Login", html);
            return new CustomResult(200, "Login success", token);
          }

          else {
            return new CustomResult(201, "Password does not match", null);
          }
        } else {
          return new CustomResult(202, "User not found", null);
        }
      } else {
        return new CustomResult(202, "User not found", null);
      }
    } catch (Exception ex) {
      ex.printStackTrace(); // Ghi lại thông báo lỗi
      return new CustomResult(400, "An error occurred", ex.getMessage());
    }
  }

  @Transactional
  @Override
  public CustomResult Register(RegisterDto res) {
    try {
      User rs = userRepo.findByEmail(res.getEmail());
      if (rs != null) {
        return new CustomResult(203, "Email ton tai", rs);
      }
      var usernew = new User();
      var userdetailnew = new UserDetail();
      BeanUtils.copyProperties(res, usernew);
      BeanUtils.copyProperties(res, userdetailnew);
      usernew.setPassword(passwordEncoder.encode(res.getPassword()));
      if (res.getAvatar() != null && !res.getAvatar().isEmpty()) {
        userdetailnew.setAvatar(firebaseStorageService.uploadFile(res.getAvatar()));
      } else {
        userdetailnew.setAvatar("22965342.jpg");
      }
      var hos = hospRepo.findByCode(res.getHospitalCode());
      if (hos.size() == 1) {
        userdetailnew.setHospital(hos.get(0));
      }
      userdetailnew.setVerify("verify");
      userdetailnew.setStatus("active");

      StompClientDto stomp = new StompClientDto();
      stomp.setAvatar(userdetailnew.getAvatar());
      stomp.setFirstName(userdetailnew.getFirstName());
      stomp.setLastName(userdetailnew.getLastName());
      stomp.setEmail(usernew.getEmail());
      stomp.setHospital(userdetailnew.getHospital().getName());
      try {
        String url = "http://localhost:8082/stompclient/create";
        CustomResult response = restTemplate.postForObject(url, stomp, CustomResult.class);
       if (response.getStatus() != 200) {
          return new CustomResult(400, "Service Chat Error", null);
        } else {
          userdetailRepo.save(userdetailnew);
          usernew.setUserDetail(userdetailnew);
          userRepo.save(usernew);
          return new CustomResult(200, "Ok", usernew);
        }
      } catch (Exception e) {
        return new CustomResult(400, "Chat Service Error", null);
      }

    } catch (Exception e) {
      return new CustomResult(400, "System Service Error", e.getMessage());
    }
  }

  @Override
  public CustomResult Logout(String token) {
    String sanitizedToken = token.replaceAll("\"", "").trim();

    if (!sanitizedToken.isEmpty()) {
      // Lấy thời gian hết hạn từ token
      long expirationTime = jwtUtil.extractExpiration(sanitizedToken).getTime();

      // Thêm token cùng với thời gian hết hạn
      var rs = inactiveTokenStore.addToken(sanitizedToken, expirationTime);
      return new CustomResult(200, "Log out success", rs);
    } else {
      return new CustomResult(400, "Log out fails", null);
    }
  }

  @Override
  public CustomResult GetAccount(FilterRes filterRes) {
    try {
      int page = filterRes.getPage() != null ? filterRes.getPage() : 0;
      int size = filterRes.getSize() != null ? filterRes.getSize() : 10;
      if (filterRes.getRole().equalsIgnoreCase("customer")) {
        Page<UserCustomerRes> userPage = userRepo.findCustomerByFilters(
            filterRes.getRole(),
            filterRes.getStatus(),
            filterRes.getSearch(),
            PageRequest.of(page, size));
        return new CustomResult(200, "Get Success", userPage);
      } else {
        Page<UserAdminRes> userPage = userRepo.findAdminByFilters(
            filterRes.getRole(),
            filterRes.getStatus(),
            filterRes.getSearch(),
            PageRequest.of(page, size));
        return new CustomResult(200, "Get Success", userPage);
      }

    } catch (Exception e) {
      return new CustomResult(400, "Get Failed", e.getMessage());
    }
  }

  @Override
  public CustomResult ChangeStatusUser(Long id) {
    try {
      var u = userRepo.findById(id);
      if (u.isPresent()) {
        User user = u.get();
        user.getUserDetail()
            .setStatus(user.getUserDetail().getStatus().equalsIgnoreCase("active") ? "deactive" : "active");
        userRepo.save(user);
        String status = user.getUserDetail().getStatus().equalsIgnoreCase("active") ? "active" : "deactive";
        String html = "<p>Notification !</p> <p>Your account has been " + status + "</p>";
        _email.sendEmail(user.getEmail(), "Medcare - Change status account", html);
       
          var keyEmail = user.getEmail().replace(".com","");
          realtime.sendRealtimeMessageToUser("account/block/" + keyEmail, status);
        
        return new CustomResult(200, "Change Status success", user);
      } else {
        return new CustomResult(300, "User not exist", null);
      }
    } catch (Exception e) {
      return new CustomResult(400, "error change status user", e.getMessage());
    }
  }

  @Override
  public CustomResult Sendmail(MailRes res) {
    try {
      String html = "<p>Message!</p> <p>" + res.getMessage() + "</p>";
      _email.sendEmail(res.getEmailTo(), res.getSubject(), html);
      return new CustomResult(200, "Send mail success", null);
    } catch (Exception e) {
      return new CustomResult(400, "error change status user", e.getMessage());
    }
  }

  public Long generateSixDigitNumber() {
    Random random = new Random();
    return 100000 + random.nextLong(900000); // Tạo số từ 100000 đến 999999
  }

  @Override
  public CustomResult Forgot(String email) {
    try {
      User u = userRepo.findByEmail(email);
      if (u != null) {
        if (!u.getUserDetail().getRole().equalsIgnoreCase("samedcare")
            && !u.getUserDetail().getRole().equalsIgnoreCase("admin")) {
          return new CustomResult(300, "User does not exist", null);
        }
        CodeVerify codeEmail = _codeVerifyRepo.findByEmail(u.getEmail());
        if (codeEmail != null) {
          codeEmail.setCode(generateSixDigitNumber());
          _codeVerifyRepo.save(codeEmail);
          var html = "<p>ResetPassword !</p> <p>CODE VERIFY: <span style='color: rgb(105, 153, 168);font-size:20px;' >"
              + codeEmail.getCode() + "</span> </p><p>The code is valid for 3 minutes</p>";
          _email.sendEmail(u.getEmail(), "ResetPassword", html);
        } else {
          CodeVerify c = new CodeVerify();
          c.setEmail(u.getEmail());
          c.setCode(generateSixDigitNumber());
          _codeVerifyRepo.save(c);
          var html = "<p>ResetPassword !</p> <p>CODE VERIFY: <span style='color: rgb(105, 153, 168);font-size:20px;' >"
              + c.getCode() + "</span> </p><p>The code is valid for 3 minutes</p>";
          _email.sendEmail(u.getEmail(), "ResetPassword", html);
        }
        return new CustomResult(200, "User ton tai", u);
      }
      return new CustomResult(300, "User does not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, "Error", e.getMessage());
    }
  }

  public String generateRandomString() {
    String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    SecureRandom random = new SecureRandom();
    int LENGTH = 8;
    StringBuilder sb = new StringBuilder(LENGTH);
    for (int i = 0; i < LENGTH; i++) {
      int index = random.nextInt(CHARACTERS.length());
      sb.append(CHARACTERS.charAt(index));
    }
    return sb.toString();
  }

  @Override
  public CustomResult ResetPassword(String code, String email) {
    try {
      CodeVerify c = _codeVerifyRepo.findByEmail(email);
      if (c != null) {
        if (c.getCode() == Long.parseLong(code)) {
          Date now = new Date();
          if (!c.getExpDate().after(now)) {
            return new CustomResult(400, "Expired code", null);
          }
          User u = userRepo.findByEmail(email);
          if (u != null) {
            var passnew = generateRandomString();
            u.setPassword(passwordEncoder.encode(passnew));
            userRepo.save(u);
            String html = "<p>Password new: <b>" + passnew
                + "</b></p> <p>Please log in to the website and change your password</p>";
            _email.sendEmail(u.getEmail(), "ResetPassword", html);
            _codeVerifyRepo.delete(c);
            return new CustomResult(200, "success", null);
          } else {
            return new CustomResult(400, "User does not exist", null);
          }
        } else {
          return new CustomResult(400, "Code does not match", null);
        }
      }
      return new CustomResult(400, "User does not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, "Error", e.getMessage());
    }
  }

  @Override
  public CustomResult UpdatePassword(UpdatePasswordDto dto) {
    try {
      var u = userRepo.findById(dto.getId());
      if (u.isPresent()) {
        User user = u.get();
        if (passwordEncoder.matches(dto.getPasswordOld(), user.getPassword())) {
          user.setPassword(passwordEncoder.encode(dto.getPasswordNew()));
          userRepo.save(user);
          String html = "<p>You have just updated your Password successfully</p>";
          _email.sendEmail(user.getEmail(), "Update Password", html);
          return new CustomResult(200, "Update Success", null);
        }
        return new CustomResult(400, "Password Old Wrong", null);
      } else {
        return new CustomResult(400, "User does not exist", null);
      }
    } catch (Exception e) {
      return new CustomResult(400, "Error", e.getMessage());
    }

  }

  @Override
  public CustomResult LoginAdmin(LoginRes res) {
    try {
      // Attempt to find the user by email
      User u = userRepo.findByEmail(res.getEmail());

      if (u != null) {
        if (!u.getUserDetail().getStatus().equalsIgnoreCase("active")) {
          return new CustomResult(201, "Account block", null);
        }
        if (u.getUserDetail().getRole().equalsIgnoreCase("admin")) {
          if (passwordEncoder.matches(res.getPassword(), u.getPassword())) {
            String token = jwtUtil.generateTokenHospital(u);
            long timestamp = System.currentTimeMillis(); // Lấy timestamp
            // realtime.sendRealtimeMessageToUser("Huy", "HeheLogin " + timestamp);
            // var html = "<p>Register Success !</p> <p>CODE VERIFY: <span style='color:
            // rgb(105, 153, 168);font-size:20px;' > 5678</span> </p>";
            // _email.sendEmail("thehuy0900@gmail.com", "Login", html);
            return new CustomResult(200, "Login success", token);
          }

          else {
            return new CustomResult(201, "Password does not match", null);
          }
        } else {
          return new CustomResult(202, "User not found", null);
        }
      } else {
        return new CustomResult(202, "User not found", null);
      }
    } catch (Exception ex) {
      ex.printStackTrace(); // Ghi lại thông báo lỗi
      return new CustomResult(400, "An error occurred", ex.getMessage());
    }
  }

  @Override
  public CustomResult UpdateInformation(UpdateInformation dto) {
    try {
      var isUser = userRepo.findById(dto.getId());
      if (isUser.isPresent()) {
        User us = isUser.get();
        UserDetail ud = us.getUserDetail();
        ud.setFirstName(dto.getFirstName());
        ud.setLastName(dto.getLastName());
        ud.setPhone(dto.getPhone());
        if (dto.getAvatar() != null) {
          ud.setAvatar(firebaseStorageService.uploadFile(dto.getAvatar()));
        }
        userdetailRepo.save(ud);
        String token = jwtUtil.generateTokenHospital(us);
        return new CustomResult(200, "Success", token);
      }
      return new CustomResult(400, "User is not Exist", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult RequestQr(String key, String email) {
    realtime.sendRealtimeMessageToUser(key, email);
    return new CustomResult(200, "Request Key Success", null);
  }

  @Override
  public CustomResult GetUsertQr(String email, String key, String location) {
    try {
      var isUser = userRepo.findByEmail(email);
      if (isUser != null) {
        InforLoginQrRes info = new InforLoginQrRes(
            isUser.getUserDetail().getFirstName(),
            isUser.getUserDetail().getLastName(),
            isUser.getUserDetail().getAvatar(),
            isUser.getEmail());

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String formattedDateTime = currentDateTime.format(formatter);
        String html = "{'keylisten':'" + key + "','location':'" + location + "','time':'" +
            formattedDateTime + "'}";
        realtime.sendRealtimeMessageToUser(key, html);
        return new CustomResult(200, "Get Success", info);
      }
      return new CustomResult(400, "Account is not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult LoginQr(String key, String token) {
    try {
      realtime.sendRealtimeMessageToUser(key, token);
      return new CustomResult(200, "Accept success", null);
    } catch (Exception e) {
      return new CustomResult(300, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult RefeshToken(String token) {
    try {
      var isEmail = jwtUtil.extractEmail(token);
      if (isEmail.isEmpty() || isEmail == "") {
        return new CustomResult(400, "Refesh Token failed", null);
      }
      var isUser = userRepo.findByEmail(isEmail);
      String tokenNew = jwtUtil.generateTokenHospital(isUser);
      return new CustomResult(200, "Refesh success", tokenNew);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

}
