package projectsem4.medcare_server.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IDoctor;
import projectsem4.medcare_server.interfaces.admin.IDoctor.DoctorDto;
import projectsem4.medcare_server.interfaces.admin.IDoctor.DoctorTop;
import projectsem4.medcare_server.interfaces.admin.IDoctor.FilterRes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/doctor")

public class DoctorController {

  @Autowired
  IDoctor _iDoctor;

  @PostMapping("getall")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetAll(@RequestBody FilterRes res) {
    return _iDoctor.GetAll(res);
  }

  @GetMapping("get/{id}")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetById(@PathVariable Long id) {
    return _iDoctor.GetById(id);
  }

  @PostMapping("create")
  @RolesAllowed({ "admin" })
  public CustomResult Create(@ModelAttribute DoctorDto dto) {
    return _iDoctor.Create(dto);
  }

  @PostMapping("update")
  @RolesAllowed({ "admin" })
  public CustomResult Update(@ModelAttribute DoctorDto dto) {
    return _iDoctor.Update(dto);
  }

  @GetMapping("changestatus/{id}")
  @RolesAllowed({ "admin" })
  public CustomResult ChangeStatus(@PathVariable Long id) {
    return _iDoctor.ChangeStatus(id);
  }

  @GetMapping("getbyhospital/{id}")
  @RolesAllowed({ "admin" })
  public CustomResult getByHospital(@PathVariable Long id) {
    return _iDoctor.GetByHospital(id);
  }

  @PostMapping("topdoctor")
  @RolesAllowed({ "admin" })
  public CustomResult TopDoctor(@RequestBody DoctorTop top) {
    return _iDoctor.TopDoctor(top);
  }
}
