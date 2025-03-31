package projectsem4.medcare_server.interfaces.customer;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface ICustomerAuth {
    CustomResult Login(LoginDTO login);

    CustomResult RegisterCustomer(RegisterCustomerDTO registerDto);

    CustomResult UpdateAvatar(UpdateAvatarCustomerDTO updateAvatarCustomerDTO);

    CustomResult ReSendVerificationCode(String email);

    CustomResult VerifyEmailCustomer(VerifyEmailCustomerDTO verifyEmailCustomerDTO);

    CustomResult UpdateUserDetailCustomer(UpdateUserDetailCustomerDTO updateUserDetailCustomerDTO);

    CustomResult UpdatePasswordCustomer(UpdatePasswordCustomerDTO updateUserDetailCustomerDTO);

    CustomResult ForgotPasswordRequestCustomer(String email);

    CustomResult ResetPasswordCustomer(VerifyEmailCustomerDTO verifyEmailCustomerDTO);

    CustomResult CreateNewToken(String email, String token);

    CustomResult TopUp(String id, String amount);

    CustomResult getBalance(String id);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UpdateUserDetailCustomerDTO {
        String email;
        String firstName;
        String lastName;
        String phone;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UpdatePasswordCustomerDTO {
        String email;
        String oldPassword;
        String newPassword;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class LoginDTO {
        String email;
        String password;
        String resetPassword;
        String type;
        String firstName;
        String lastName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class RegisterCustomerDTO {
        String email;
        String password;
        String firstName;
        String lastName;
        String phone;
        MultipartFile avatar;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UpdateAvatarCustomerDTO {
        String id;
        MultipartFile avatar;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class VerifyEmailCustomerDTO {
        String email;
        String code;
    }
}
