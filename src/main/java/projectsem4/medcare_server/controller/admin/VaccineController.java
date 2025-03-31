package projectsem4.medcare_server.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IVaccine;
import projectsem4.medcare_server.interfaces.admin.IVaccine.FilterRes;
import projectsem4.medcare_server.interfaces.admin.IVaccine.VaccineDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/vaccine")
public class VaccineController {
  @Autowired
  IVaccine _IVaccine;

  @PostMapping("getbyhospital")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetAll(@RequestBody FilterRes res) {
    return _IVaccine.GetByHospital(res);
  }

  @GetMapping("get/{id}")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetById(@PathVariable Long id) {
    return _IVaccine.GetById(id);
  }

  @PostMapping("create")
  @RolesAllowed({ "admin" })
  public CustomResult Create(@RequestBody VaccineDto dto) {
    return _IVaccine.Create(dto);
  }

  @PostMapping("update")
  @RolesAllowed({ "admin" })
  public CustomResult Update(@RequestBody VaccineDto dto) {
    return _IVaccine.Update(dto);
  }

  @GetMapping("changestatus/{id}")
  @RolesAllowed({ "admin" })
  public CustomResult ChangeStatus(@PathVariable Long id) {
    return _IVaccine.ChangeStatus(id);
  }

}
