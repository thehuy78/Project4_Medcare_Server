package projectsem4.medcare_server.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IAnalysis;
import projectsem4.medcare_server.interfaces.admin.IAnalysis.Chart1Dto;
import projectsem4.medcare_server.interfaces.admin.IAnalysis.ChartCompareDto;
import projectsem4.medcare_server.interfaces.admin.IAnalysis.ChartMapDto;
import projectsem4.medcare_server.interfaces.admin.IAnalysis.ChartMapEvent;
import projectsem4.medcare_server.interfaces.admin.IAnalysis.ChartMapVN;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/analysis")
public class AnalysisController {

  @Autowired
  IAnalysis _IAnalysis;

  @PostMapping("chart1")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult RawChart(@RequestBody Chart1Dto chartDto) {
    return _IAnalysis.Chart1(chartDto);
  }

  @PostMapping("compareChart")
  @RolesAllowed({ "samedcare" })
  public CustomResult CompareDrawChart(@RequestBody ChartCompareDto chartDto) {
    return _IAnalysis.ChartCompare(chartDto);
  }

  @PostMapping("serviceHospital")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult CompareService(@RequestBody Chart1Dto chartDto) {
    return _IAnalysis.AnalytisAllServiceHospital(chartDto);
  }

  @PostMapping("chartMap")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult ChartMap(@RequestBody ChartMapDto chartDto) {
    return _IAnalysis.ChartMapVN(chartDto);
  }

  @PostMapping("chartMapEvent")
  @RolesAllowed({ "samedcare" })
  public CustomResult ChartMap(@RequestBody ChartMapEvent chartDto) {
    return _IAnalysis.ChartMapVNEvent(chartDto);
  }
}
