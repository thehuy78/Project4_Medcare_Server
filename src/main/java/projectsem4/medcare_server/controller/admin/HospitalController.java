package projectsem4.medcare_server.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IHospital;
import projectsem4.medcare_server.interfaces.admin.IHospital.FilterRes;
import projectsem4.medcare_server.interfaces.admin.IHospital.HospitalDto;
import projectsem4.medcare_server.interfaces.admin.IHospital.HospitalUpdateDto;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/hospital")

public class HospitalController {

  @Autowired
  IHospital _iHospital;

  @PostMapping("getall")
  @RolesAllowed({ "samedcare" })
  public CustomResult getall(@RequestBody FilterRes res) {

    return _iHospital.GetAll(res);
  }

  @PostMapping("create")
  @RolesAllowed({ "samedcare" })
  public CustomResult postMethodName(@ModelAttribute HospitalDto h) {
    return _iHospital.Create(h);
  }

  @PostMapping("update")
  @RolesAllowed({ "samedcare" })
  public CustomResult updateHospital(@ModelAttribute HospitalUpdateDto h) {
    return _iHospital.Update(h);
  }

  @GetMapping("getname")
  @RolesAllowed({ "samedcare" })
  public CustomResult getnameHospital() {
    return _iHospital.GetNameAndCode();
  }

  @GetMapping("changestatus/{id}")
  @RolesAllowed({ "samedcare" })
  public CustomResult changeStatusHospital(@PathVariable Long id) {
    return _iHospital.ChangeStatusHospital(id);
  }

  @GetMapping("getbyid/{id}")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult getById(@PathVariable Long id) {
    return _iHospital.GetById(id);
  }

}
