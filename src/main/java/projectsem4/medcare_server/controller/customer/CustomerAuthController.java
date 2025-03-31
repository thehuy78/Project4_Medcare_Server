package projectsem4.medcare_server.controller.customer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IUser.RegisterDto;
import projectsem4.medcare_server.interfaces.customer.ICustomerAuth;
import projectsem4.medcare_server.interfaces.customer.ICustomerAuth.LoginDTO;
import projectsem4.medcare_server.interfaces.customer.ICustomerAuth.RegisterCustomerDTO;
import projectsem4.medcare_server.interfaces.customer.ICustomerAuth.UpdateAvatarCustomerDTO;
import projectsem4.medcare_server.interfaces.customer.ICustomerAuth.UpdatePasswordCustomerDTO;
import projectsem4.medcare_server.interfaces.customer.ICustomerAuth.UpdateUserDetailCustomerDTO;
import projectsem4.medcare_server.interfaces.customer.ICustomerAuth.VerifyEmailCustomerDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/customer/auth")
public class CustomerAuthController {

    @Autowired
    ICustomerAuth _ICustomerAuth;

    @PostMapping("login")
    public ResponseEntity<CustomResult> postMethodName(@ModelAttribute LoginDTO loginDTO) {

        var result = _ICustomerAuth.Login(loginDTO);
        if (result.getStatus() == 200 || result.getStatus() == 205 || result.getStatus() == 206
                || result.getStatus() == 207 || result.getStatus() == 208||result.getStatus() == 210) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("register")
    public ResponseEntity<CustomResult> register(@ModelAttribute RegisterCustomerDTO registerDto) {
        var result = _ICustomerAuth.RegisterCustomer(registerDto);
        if (result.getStatus() == 200 || result.getStatus() == 201) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("updateavatar")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> updateavatar(@ModelAttribute UpdateAvatarCustomerDTO updateAvatarCustomerDTO) {
        var result = _ICustomerAuth.UpdateAvatar(updateAvatarCustomerDTO);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("resendverificationcode/{email}")
    public ResponseEntity<CustomResult> resendverificationcode(@PathVariable String email) {
        var result = _ICustomerAuth.ReSendVerificationCode(email);
        if (result.getStatus() == 200 || result.getStatus() == 201) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("verificationEmail")
    public ResponseEntity<CustomResult> verificationEmail(@ModelAttribute VerifyEmailCustomerDTO verificationEmail) {
        var result = _ICustomerAuth.VerifyEmailCustomer(verificationEmail);
        if (result.getStatus() == 200 || result.getStatus() == 201 || result.getStatus() == 202) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("updateUserDetail")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> updateUserDetail(
            @ModelAttribute UpdateUserDetailCustomerDTO updateUserDetailCustomerDTO) {
        var result = _ICustomerAuth.UpdateUserDetailCustomer(updateUserDetailCustomerDTO);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("updatePassword")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> updatePassword(
            @ModelAttribute UpdatePasswordCustomerDTO updatePasswordCustomerDTO) {
        var result = _ICustomerAuth.UpdatePasswordCustomer(updatePasswordCustomerDTO);
        if (result.getStatus() == 200 || result.getStatus() == 201) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("forgotPasswordRequestCustomer/{email}")
    public ResponseEntity<CustomResult> forgotPasswordRequestCustomer(@PathVariable String email) {
        var result = _ICustomerAuth.ForgotPasswordRequestCustomer(email);
        if (result.getStatus() == 200 || result.getStatus() == 201) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("topup")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> topup(@RequestParam String id, @RequestParam String amount) {
        var result = _ICustomerAuth.TopUp(id, amount);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getBalance")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> getBalance(@RequestParam String id) {
        var result = _ICustomerAuth.getBalance(id);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("resetPassword")
    public ResponseEntity<CustomResult> resetPassword(@ModelAttribute VerifyEmailCustomerDTO verificationEmail) {
        var result = _ICustomerAuth.ResetPasswordCustomer(verificationEmail);
        if (result.getStatus() == 200 || result.getStatus() == 201 || result.getStatus() == 202) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

}
