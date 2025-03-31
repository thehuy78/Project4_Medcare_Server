package projectsem4.medcare_server.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IReport;
import projectsem4.medcare_server.interfaces.admin.IReport.FilterRes;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/report")
public class ReportController {
  @Autowired
  IReport _IReport;

  @PostMapping("booking")
  public CustomResult postMethodName(@RequestBody FilterRes filter) {
    return _IReport.ExportFile(filter);
  }

}
