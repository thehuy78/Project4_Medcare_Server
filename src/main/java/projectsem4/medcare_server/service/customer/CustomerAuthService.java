package projectsem4.medcare_server.service.customer;

import java.net.URI;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.mapper.Mapper.Null;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.CodeVerify;
import projectsem4.medcare_server.domain.entity.Notification;
import projectsem4.medcare_server.domain.entity.Transaction;
import projectsem4.medcare_server.domain.entity.User;
import projectsem4.medcare_server.domain.entity.UserDetail;
import projectsem4.medcare_server.interfaces.customer.ICustomerAuth;
import projectsem4.medcare_server.repository.customer.CodeVerifyRepo;
import projectsem4.medcare_server.repository.customer.CustomerAuthRepo;
import projectsem4.medcare_server.repository.customer.CustomerNotificationRepo;
import projectsem4.medcare_server.repository.customer.CustomerTransactionRepo;
import projectsem4.medcare_server.repository.customer.CustomerUserDetailRepo;
import projectsem4.medcare_server.security.JwtUtil;
import projectsem4.medcare_server.service.EmailService;
import projectsem4.medcare_server.service.FirebaseRealtimeDatabaseService;
import projectsem4.medcare_server.service.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Value;

@Service
public class CustomerAuthService implements ICustomerAuth {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    CustomerAuthRepo _CustomerAuthRepo;

    @Autowired
    CodeVerifyRepo _CodeVerifyRepo;

    @Autowired
    CustomerUserDetailRepo _CustomerUserDetailRepo;

    @Autowired
    CustomerTransactionRepo _CustomerTransactionRepo;

    @Autowired
    FirebaseStorageService firebaseStorageService;

    @Autowired
    EmailService _email;
    @Autowired
    FirebaseRealtimeDatabaseService _FirebaseRealtimeDatabaseService;

    @Autowired
    CustomerNotificationRepo _CustomerNotificationRepo;

    @Value("${nexmo.api.key}")
    private String apiKey;

    @Value("${nexmo.api.secret}")
    private String apiSecret;

    @Override
    public CustomResult Login(LoginDTO login) {
        try {
            if (login.getType() != null && login.getType().equals("null")) {
                User u = _CustomerAuthRepo.findByEmail(login.getEmail());
                if (u != null && u.getUserDetail().getRole().equalsIgnoreCase("customer")
                        && u.getUserDetail().getStatus().equals("deactive")) {
                    return new CustomResult(210, "account block", null);
                } else {
                    if (u != null && u.getUserDetail().getRole().equalsIgnoreCase("customer")) {
                        if (passwordEncoder.matches(login.getPassword(), u.getPassword())) {
                            if (u.getUserDetail().getVerify().equals("verified")) {
                                String token = jwtUtil.generateTokenUser(u);
                                if (login.getResetPassword() != null) {
                                    return new CustomResult(206, "Login success", token);
                                }
                                return new CustomResult(200, "Login success", token);
                            } else {
                                return new CustomResult(205, "Not verify yet", null);
                            }

                        } else {
                            return new CustomResult(207, "Email or Password wrong", null);
                        }
                    } else {
                        return new CustomResult(207, "Email or Password wrong", null);
                    }
                }
            } else {
                User u = _CustomerAuthRepo.findByEmail(login.getEmail());
                if (u != null && u.getUserDetail().getRole().equalsIgnoreCase("customer")) {
                    String token = jwtUtil.generateTokenUser(u);
                    return new CustomResult(208, "Login success", token);
                } else {
                    User newUser = new User();
                    newUser.setEmail(login.getEmail());
                    String newPassString = generateRandomString();
                    newUser.setPassword(passwordEncoder.encode(newPassString));
                    UserDetail newUserDetail = new UserDetail();
                    newUserDetail.setBalance(0.0);
                    newUserDetail.setFirstName(login.getFirstName());
                    newUserDetail.setLastName(login.getLastName());
                    newUserDetail.setRole("customer");
                    newUserDetail.setVerify("verified");
                    newUserDetail.setCreateDate(new Date());
                    newUserDetail.setStatus("active");
                    newUser.setUserDetail(newUserDetail);
                    _CustomerUserDetailRepo.save(newUserDetail);
                    _CustomerAuthRepo.save(newUser);
                    String token = jwtUtil.generateTokenUser(newUser);
                    return new CustomResult(208, "Login success", token);
                }
            }

        } catch (Exception e) {
            return new CustomResult(402, "Server error", "");
        }
    }

    public Long generateSixDigitNumber() {
        Random random = new Random();
        return 100000 + random.nextLong(900000); // Tạo số từ 100000 đến 999999
    }

    @Override
    public CustomResult RegisterCustomer(RegisterCustomerDTO registerDtoDTO) {
        try {
            User u = _CustomerAuthRepo.findByEmail(registerDtoDTO.getEmail());
            if (u != null) {
                return new CustomResult(201, "Duplicate Email", "");
            }
            User newUser = new User();
            newUser.setEmail(registerDtoDTO.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerDtoDTO.getPassword().trim()));
            UserDetail newUserDetail = new UserDetail();
            newUserDetail.setBalance(0.0);
            newUserDetail.setFirstName(registerDtoDTO.getFirstName());
            newUserDetail.setLastName(registerDtoDTO.getLastName());
            newUserDetail.setRole("customer");
            newUserDetail.setVerify("verified");
            newUserDetail.setCreateDate(new Date());
            newUserDetail.setStatus("deactive");
            newUserDetail.setPhone(registerDtoDTO.getPhone());
            if (registerDtoDTO.getAvatar() != null) {
                newUserDetail.setAvatar(firebaseStorageService.uploadFile(registerDtoDTO.getAvatar()));
            } else {
                newUserDetail.setAvatar("22965342.jpg");
            }

            newUser.setUserDetail(newUserDetail);
            _CustomerUserDetailRepo.save(newUserDetail);
            _CustomerAuthRepo.save(newUser);
            CodeVerify c = new CodeVerify();
            c.setEmail(registerDtoDTO.getEmail());
            c.setCode(generateSixDigitNumber());
            _CodeVerifyRepo.save(c);
            var html = "<p>Register !</p> <p>CODE VERIFY: <span style='color: rgb(105, 153, 168);font-size:20px;' >"
                    + c.getCode() + "</span> </p><p>The code is valid for 3 minutes</p>";
            _email.sendEmail(registerDtoDTO.getEmail(), "Verify Register", html);
            // VonageClient client =
            // VonageClient.builder().apiKey(apiKey).apiSecret(apiSecret).build();

            // // Gửi tin nhắn SMS
            // TextMessage textMessage = new TextMessage("VonageAPI", "+84934522407",
            // "hello");
            // SmsSubmissionResponse response =
            // client.getSmsClient().submitMessage(textMessage);

            // if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            // System.out.println("Message sent successfully.");
            // } else {
            // System.out.println("Message failed with error: " +
            // response.getMessages().get(0).getErrorText());
            // }

            return new CustomResult(200, "Success", "");

        } catch (Exception e) {
            return new CustomResult(402, "Server error", "");

        }
    }

    @Override
    public CustomResult UpdateAvatar(UpdateAvatarCustomerDTO updateAvatarCustomerDTO) {

        try {
            User u = _CustomerAuthRepo.findById(Long.parseLong(updateAvatarCustomerDTO.getId())).get();
            UserDetail userDetail = u.getUserDetail();
            userDetail.setAvatar(firebaseStorageService.uploadFile(updateAvatarCustomerDTO.getAvatar()));
            _CustomerUserDetailRepo.save(userDetail);
            Notification notification = new Notification();
            notification.setType("customer:update");
            notification.setStatus("unread");
            notification.setIsDeleted("undeleted");
            notification.setUser(u);
            notification.setDescription("Update Avatar");
            _CustomerNotificationRepo.save(notification);
            Long timestamp = System.currentTimeMillis();
            _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser(
                    "notificationCustomer/" + u.getId().toString(),
                    timestamp.toString());
            return new CustomResult(200, "Success", userDetail.getAvatar());

        } catch (Exception e) {
            return new CustomResult(402, "Server error", "");
        }
    }

    @Override
    public CustomResult ReSendVerificationCode(String email) {
        try {
            User u = _CustomerAuthRepo.findByEmail(email);
            if (u != null) {
                if (!u.getUserDetail().getRole().equalsIgnoreCase("customer")) {
                    return new CustomResult(300, "User does not exist", null);
                }
                CodeVerify codeEmail = _CodeVerifyRepo.findByEmail(u.getEmail());
                if (codeEmail != null) {
                    codeEmail.setCode(generateSixDigitNumber());
                    _CodeVerifyRepo.save(codeEmail);
                    var html = "<p>ReSend Code !</p> <p>CODE VERIFY: <span style='color: rgb(105, 153, 168);font-size:20px;' >"
                            + codeEmail.getCode() + "</span> </p><p>The code is valid for 3 minutes</p>";
                    _email.sendEmail(u.getEmail(), "Verify Code", html);
                } else {
                    CodeVerify c = new CodeVerify();
                    c.setEmail(u.getEmail());
                    c.setCode(generateSixDigitNumber());
                    _CodeVerifyRepo.save(c);
                    var html = "<p>ReSend Code !</p> <p>CODE VERIFY: <span style='color: rgb(105, 153, 168);font-size:20px;' >"
                            + c.getCode() + "</span> </p><p>The code is valid for 3 minutes</p>";
                    _email.sendEmail(u.getEmail(), "Verify Code", html);
                }
                return new CustomResult(200, "Success", null);
            }
            return new CustomResult(201, "User does not exist", null);
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", e.getMessage());
        }
    }

    @Override
    public CustomResult VerifyEmailCustomer(VerifyEmailCustomerDTO verifyEmailCustomerDTO) {
        try {
            CodeVerify c = _CodeVerifyRepo.findByEmail(verifyEmailCustomerDTO.getEmail());
            if (c != null) {
                if (c.getCode() == Long.parseLong(verifyEmailCustomerDTO.getCode())) {
                    Date now = new Date();
                    if (!c.getExpDate().after(now)) {
                        return new CustomResult(202, "Expired code", null);
                    } else {
                        User u = _CustomerAuthRepo.findByEmail(verifyEmailCustomerDTO.getEmail());
                        UserDetail userDetail = u.getUserDetail();
                        userDetail.setVerify("verified");
                        userDetail.setStatus("active");
                        _CustomerUserDetailRepo.save(userDetail);
                        String token = jwtUtil.generateTokenUser(u);
                        return new CustomResult(200, "Success", token);
                    }
                } else {
                    return new CustomResult(201, "Wrong Code", null);
                }
            } else {
                return new CustomResult(202, "Expired code", null);
            }
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult UpdateUserDetailCustomer(UpdateUserDetailCustomerDTO updateUserDetailCustomerDTO) {
        try {
            User u = _CustomerAuthRepo.findByEmail(updateUserDetailCustomerDTO.getEmail());

            if (u == null) {
                return new CustomResult(400, "Not Found", "");
            }
            UserDetail userdetail = u.getUserDetail();
            Notification notification = new Notification();
            if (updateUserDetailCustomerDTO.getFirstName().trim().length() > 0) {
                userdetail.setFirstName(updateUserDetailCustomerDTO.getFirstName().trim());

                notification.setType("customer:update");
                notification.setStatus("unread");
                notification.setIsDeleted("undeleted");
                notification.setUser(u);
                notification.setDescription("Update First Name");
                Long timestamp = System.currentTimeMillis();
                _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser(
                        "notificationCustomer/" + u.getId().toString(),
                        timestamp.toString());
            }
            if (updateUserDetailCustomerDTO.getLastName().trim().length() > 0) {
                userdetail.setLastName(updateUserDetailCustomerDTO.getLastName().trim());

                notification.setType("customer:update");
                notification.setStatus("active");
                notification.setIsDeleted("deactive");
                notification.setUser(u);
                notification.setDescription("Update Last Name");
                Long timestamp = System.currentTimeMillis();
                _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser(
                        "notificationCustomer/" + u.getId().toString(),
                        timestamp.toString());
            }
            if (updateUserDetailCustomerDTO.getPhone().trim().length() > 0) {
                userdetail.setPhone(updateUserDetailCustomerDTO.getPhone().trim());

                notification.setType("customer:update");
                notification.setStatus("active");
                notification.setIsDeleted("deactive");
                notification.setUser(u);
                notification.setDescription("Update Phone");
                Long timestamp = System.currentTimeMillis();
                _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser(
                        "notificationCustomer/" + u.getId().toString(),
                        timestamp.toString());
            }
            _CustomerUserDetailRepo.save(userdetail);
            _CustomerNotificationRepo.save(notification);
            return new CustomResult(200, "Success", "");
        } catch (Exception e) {
            return new CustomResult(404, "Server Error", "");
        }
    }

    @Override
    public CustomResult UpdatePasswordCustomer(UpdatePasswordCustomerDTO updateUserDetailCustomerDTO) {
        try {
            User u = _CustomerAuthRepo.findByEmail(updateUserDetailCustomerDTO.getEmail());

            if (u == null) {
                return new CustomResult(400, "Not Found", "");
            }
            if (passwordEncoder.matches(updateUserDetailCustomerDTO.getOldPassword(), u.getPassword())) {
                u.setPassword(passwordEncoder.encode(updateUserDetailCustomerDTO.getNewPassword()));
                _CustomerAuthRepo.save(u);
                Notification notification = new Notification();
                notification.setType("customer:update");
                notification.setStatus("unread");
                notification.setIsDeleted("undeleted");
                notification.setUser(u);
                notification.setDescription("Update Password");
                Long timestamp = System.currentTimeMillis();
                _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser(
                        "notificationCustomer/" + u.getId().toString(),
                        timestamp.toString());
                _CustomerNotificationRepo.save(notification);
                return new CustomResult(200, "Success", "");
            } else {
                return new CustomResult(201, "Wrong Password", "");
            }
        } catch (Exception e) {
            return new CustomResult(404, "Server error", "");
        }
    }

    @Override
    public CustomResult ForgotPasswordRequestCustomer(String email) {
        try {
            User u = _CustomerAuthRepo.findByEmail(email);
            if (u == null) {
                return new CustomResult(201, "Not Found", "");
            }
            CodeVerify codeEmail = _CodeVerifyRepo.findByEmail(u.getEmail());
            if (codeEmail != null) {
                codeEmail.setCode(generateSixDigitNumber());
                _CodeVerifyRepo.save(codeEmail);
                var html = "<p>Reset Password !</p> <p>CODE VERIFY: <span style='color: rgb(105, 153, 168);font-size:20px;' >"
                        + codeEmail.getCode() + "</span> </p><p>The code is valid for 3 minutes</p>";
                _email.sendEmail(email, "Verify Code", html);
            } else {
                CodeVerify c = new CodeVerify();
                c.setEmail(u.getEmail());
                c.setCode(generateSixDigitNumber());
                _CodeVerifyRepo.save(c);
                var html = "<p>Reset Password !</p> <p>CODE VERIFY: <span style='color: rgb(105, 153, 168);font-size:20px;' >"
                        + c.getCode() + "</span> </p><p>The code is valid for 3 minutes</p>";
                _email.sendEmail(email, "Verify Code", html);
            }
            return new CustomResult(200, "Success", null);
        } catch (Exception e) {
            return new CustomResult(404, "Server error", "");
        }
    }

    public static String generateRandomString() {
        String CHARACTERS = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz0123456789";
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder(9);
        for (int i = 0; i < 9; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    @Override
    public CustomResult ResetPasswordCustomer(VerifyEmailCustomerDTO verifyEmailCustomerDTO) {
        try {
            CodeVerify c = _CodeVerifyRepo.findByEmail(verifyEmailCustomerDTO.getEmail());
            if (c != null) {
                if (c.getCode() == Long.parseLong(verifyEmailCustomerDTO.getCode())) {
                    Date now = new Date();
                    if (!c.getExpDate().after(now)) {
                        return new CustomResult(202, "Expired code", null);
                    } else {
                        User u = _CustomerAuthRepo.findByEmail(verifyEmailCustomerDTO.getEmail());
                        String newPassString = generateRandomString();
                        u.setPassword(passwordEncoder.encode(newPassString));
                        _CustomerAuthRepo.save(u);
                        var html = "<p>Reset Password !</p> <p>Your New Password : <span style='color: rgb(105, 153, 168);font-size:20px;' >"

                                + newPassString + "</span>";
                        _email.sendEmail(verifyEmailCustomerDTO.getEmail(), "Reset Password", html);

                        Notification notification = new Notification();
                        notification.setType("customer:update");
                        notification.setStatus("unread");
                        notification.setIsDeleted("undeleted");
                        notification.setUser(u);
                        notification.setDescription("Reset Password");
                        Long timestamp = System.currentTimeMillis();
                        _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser(
                                "notificationCustomer/" + u.getId().toString(),
                                timestamp.toString());

                        _CustomerNotificationRepo.save(notification);

                        return new CustomResult(200, "Success", null);
                    }
                } else {
                    return new CustomResult(201, "Wrong Code", null);
                }
            } else {
                return new CustomResult(202, "Expired code", null);
            }
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult CreateNewToken(String email, String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'CreateNewToken'");
    }

    @Override
    public CustomResult TopUp(String id, String amount) {
        try {
            User u = _CustomerAuthRepo.findById(Long.parseLong(id)).get();
            UserDetail ud = _CustomerUserDetailRepo.findById(Long.parseLong(id)).get();
            ud.setBalance(ud.getBalance() + Double.parseDouble(amount));
            _CustomerUserDetailRepo.save(ud);
            Transaction transaction = new Transaction();
            transaction.setBalance(ud.getBalance());
            transaction.setDescription(u.getEmail() + " Top up");
            transaction.setTraded(Double.parseDouble(amount));
            transaction.setStatus("active");
            transaction.setUser(u);
            _CustomerTransactionRepo.save(transaction);
            Notification notification = new Notification();
            notification.setType("customer:update");
            notification.setStatus("unread");
            notification.setIsDeleted("undeleted");
            notification.setUser(u);
            notification.setDescription("Top up: " + amount + ".VND");
            Long timestamp = System.currentTimeMillis();
            _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser(
                    "notificationCustomer/" + u.getId().toString(),
                    timestamp.toString());
            _CustomerNotificationRepo.save(notification);
            return new CustomResult(200, "Success", ud.getBalance().toString());

        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult getBalance(String id) {
        try {
            User user = _CustomerAuthRepo.findById(Long.parseLong(id)).get();
            UserDetail ud = user.getUserDetail();
            return new CustomResult(200, "Success", ud.getBalance().toString());
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

}
