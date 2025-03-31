package projectsem4.medcare_server.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IType;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/admin/type")
public class TypeController {
  @Autowired
  IType _IType;

  @GetMapping("getTypeBooking")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult getMethodName() {
    return _IType.GetTypeBooking();
  }

  @GetMapping("getTypeHospital")
  @RolesAllowed({ "samedcare" })
  public CustomResult getTypeHospital() {
    return _IType.GetTypeHospital();
  }

}
