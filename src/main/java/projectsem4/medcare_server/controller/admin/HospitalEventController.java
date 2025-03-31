package projectsem4.medcare_server.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IHospitalEvent;
import projectsem4.medcare_server.interfaces.admin.IHospitalEvent.AddDto;
import projectsem4.medcare_server.interfaces.admin.IHospitalEvent.Filter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/hospitalEvent")
public class HospitalEventController {

  @Autowired
  IHospitalEvent _IHospitalEvent;

  @PostMapping("getByEvent")
  public CustomResult GetByEvent(@RequestBody Filter filter) {
    return _IHospitalEvent.GetByEvent(filter);
  }

  @PostMapping("requestAddVaccine")
  public CustomResult SendRequestAddVaccine(@RequestBody AddDto dto) {
    return _IHospitalEvent.AddHospitalEvent(dto);
  }

  @GetMapping("remove/{id}")
  public CustomResult SendRequestAddVaccine(@PathVariable Long id) {
    return _IHospitalEvent.RemoveHospitalEvent(id);
  }

  @GetMapping("changeStatus/{id}")
  public CustomResult Accept(@PathVariable Long id) {
    return _IHospitalEvent.ChangeStatus(id);
  }

  @GetMapping("denied/{id}")
  public CustomResult Denied(@PathVariable Long id) {
    return _IHospitalEvent.Denied(id);
  }

}
