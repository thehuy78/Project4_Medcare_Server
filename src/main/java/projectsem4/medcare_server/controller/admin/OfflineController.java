package projectsem4.medcare_server.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IOffline;
import projectsem4.medcare_server.interfaces.admin.IPack;
import projectsem4.medcare_server.interfaces.admin.IOffline.OfflineDto;
import projectsem4.medcare_server.interfaces.admin.IOffline.OfflineFilter;
import projectsem4.medcare_server.interfaces.admin.IPack.FilterRes;

@RestController
@RequestMapping("/api/admin/offline")
public class OfflineController {
  @Autowired
  IOffline _iOffline;

  @PostMapping("create")
  @RolesAllowed({ "admin" })
  public CustomResult Create(@RequestBody OfflineDto dto) {
    return _iOffline.Create(dto);
  }

  @PostMapping("get")
  @RolesAllowed({ "admin" })
  public CustomResult GetByHospital(@RequestBody OfflineFilter filter) {
    return _iOffline.GetByHospital(filter);
  }
}
