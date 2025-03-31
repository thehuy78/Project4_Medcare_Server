package projectsem4.medcare_server.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.ITesting;
import projectsem4.medcare_server.interfaces.admin.ITesting.FilterRes;
import projectsem4.medcare_server.interfaces.admin.ITesting.TestingDto;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/admin/testing")
public class TestingController {

  @Autowired
  ITesting _ITesting;

  @PostMapping("getbyhospital")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult postMethodName(@RequestBody FilterRes res) {
    return _ITesting.GetByHospital(res);
  }

  @GetMapping("get/{id}")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetById(@PathVariable Long id) {
    return _ITesting.GetById(id);
  }

  @PostMapping("create")
  @RolesAllowed({ "admin" })
  public CustomResult Create(@RequestBody TestingDto dto) {
    return _ITesting.Create(dto);
  }

  @PostMapping("update")
  @RolesAllowed({ "admin" })
  public CustomResult Update(@RequestBody TestingDto dto) {
    return _ITesting.Update(dto);
  }

  @GetMapping("changestatus/{id}")
  @RolesAllowed({ "admin" })
  public CustomResult ChangeStatus(@PathVariable Long id) {
    return _ITesting.ChangeStatus(id);
  }
}
