package projectsem4.medcare_server.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IEventVaccine.*;
import projectsem4.medcare_server.interfaces.admin.IEventVaccine;;

@RestController
@RequestMapping("/api/admin/eventVaccine")
public class EventVaccineController {

  @Autowired
  IEventVaccine _IEventVaccine;

  @PostMapping("getAll")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetAll(@RequestBody Filter filter) {
    return _IEventVaccine.GetAll(filter);
  }

  @GetMapping("get/{id}")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetById(@PathVariable Long id) {
    return _IEventVaccine.GetById(id);
  }

  @PostMapping("create")
  @RolesAllowed({ "samedcare" })
  public CustomResult create(@RequestBody EventVaccineDto dto) {
    return _IEventVaccine.Create(dto);
  }

  @PostMapping("update")
  @RolesAllowed({ "samedcare" })
  public CustomResult update(@RequestBody EventVaccineDto dto) {
    return _IEventVaccine.Update(dto);
  }

  @GetMapping("changeStatus/{id}")
  @RolesAllowed({ "samedcare" })
  public CustomResult change(@PathVariable Long id) {
    return _IEventVaccine.ChangeStatus(id);
  }

}
