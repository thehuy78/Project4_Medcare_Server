package projectsem4.medcare_server.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.websocket.server.PathParam;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IPack;
import projectsem4.medcare_server.interfaces.admin.IPack.FilterRes;
import projectsem4.medcare_server.interfaces.admin.IPack.PackageDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/admin/pack")
public class PackController {
  @Autowired
  IPack _iPack;

  @PostMapping("getbyhospital")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetByHospital(@RequestBody FilterRes res) {

    return _iPack.GetByHospital(res);
  }

  @GetMapping("get/{id}")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetById(@PathVariable Long id) {
    return _iPack.GetById(id);
  }

  @PostMapping("create")
  @RolesAllowed({ "admin" })
  public CustomResult Create(@RequestBody PackageDto dto) {
    return _iPack.Create(dto);
  }

  @PostMapping("update")
  @RolesAllowed({ "admin" })
  public CustomResult Update(@RequestBody PackageDto dto) {
    return _iPack.Update(dto);
  }

  @GetMapping("changestatus/{id}")
  @RolesAllowed({ "admin" })
  public CustomResult ChangeStatus(@PathVariable Long id) {
    return _iPack.ChangeStatus(id);
  }

}
