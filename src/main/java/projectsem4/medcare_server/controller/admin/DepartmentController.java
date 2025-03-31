package projectsem4.medcare_server.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IDepartment;
import projectsem4.medcare_server.interfaces.admin.IDepartment.DepartmentDto;
import projectsem4.medcare_server.interfaces.admin.IDepartment.FilterRes;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/department")

public class DepartmentController {

  @Autowired
  IDepartment _iDepartment;

  @PostMapping("getall")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult Getall(@RequestBody FilterRes res) {
    return _iDepartment.GetAll(res);
  }

  @PostMapping("create")
  @RolesAllowed({ "admin" })
  public CustomResult Create(@RequestBody DepartmentDto dto) {
    return _iDepartment.Create(dto);
  }

  @PostMapping("update")
  @RolesAllowed({ "admin" })
  public CustomResult Update(@RequestBody DepartmentDto dto) {
    return _iDepartment.Update(dto);
  }

  @GetMapping("changestatus/{id}")
  @RolesAllowed({ "admin" })
  public CustomResult ChangeStatus(@PathVariable Long id) {
    return _iDepartment.ChangeStatus(id);
  }

}
