package projectsem4.medcare_server.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IServices;
import projectsem4.medcare_server.interfaces.admin.IServices.AttributeRes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/services")
public class ServicesController {

  @Autowired
  IServices _IServices;

  @GetMapping("getall")
  @RolesAllowed({ "samedcare" })
  public CustomResult getMethodName(
      @RequestParam(defaultValue = "0") Integer pageNumber,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "") String search) {
    return _IServices.GetHospitalService(pageNumber, pageSize, search);
  }

  @GetMapping("getservice")
  @RolesAllowed({ "samedcare" })
  public CustomResult getservice() {
    return _IServices.GetService();
  }

  @GetMapping("servicehospital/{code}")
  @RolesAllowed({ "admin" })
  public CustomResult getserviceByHospital(@PathVariable String code) {
    return _IServices.GetServiceByHospital(code);
  }

  @GetMapping("changestatus/{id}")
  @RolesAllowed({ "samedcare" })
  public CustomResult changeStatus(@PathVariable Long id) {
    return _IServices.ChangeStatusService(id);
  }

  @GetMapping("addservice/{name}")
  @RolesAllowed({ "samedcare" })
  public CustomResult add(@PathVariable String name) {
    return _IServices.AddService(name);
  }

  @PostMapping("editservice")
  @RolesAllowed({ "samedcare" })
  public CustomResult edit(@RequestBody AttributeRes res) {
    return _IServices.EditService(res);
  }

}
