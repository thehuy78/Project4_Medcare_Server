package projectsem4.medcare_server.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IAccountant;
import projectsem4.medcare_server.interfaces.admin.IAccountant.FilterAccountant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/accountant")
public class AccountantController {

  @Autowired
  IAccountant _IAccountant;

  @GetMapping("getWeek/{hospitalId}")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetWeekAccountant(@PathVariable String hospitalId) {
    return _IAccountant.GetWeekAccountant(hospitalId);
  }

  @GetMapping("getMonth/{hospitalId}")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetMonthAccountant(@PathVariable String hospitalId) {
    return _IAccountant.GetMonthAccountant(hospitalId);
  }

  @PostMapping("export")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetMonthAccountant(@RequestBody FilterAccountant filter) {
    return _IAccountant.ExportFile(filter);
  }

}
