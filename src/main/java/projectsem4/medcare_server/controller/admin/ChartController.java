package projectsem4.medcare_server.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IChart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/admin/chart")

public class ChartController {

  @Autowired
  IChart _IChart;

  @GetMapping("revenueprofit")
  @RolesAllowed({ "samedcare" })
  public CustomResult getMethodName() {
    return _IChart.TotalRevenue();
  }

  @GetMapping("revenue/{code}")
  @RolesAllowed({ "admin" })
  public CustomResult GetRevenueHospital(@PathVariable String code) {
    return _IChart.TotalRevenueHospital(code);
  }

  @GetMapping("gender/{day}")
  @RolesAllowed({ "samedcare" })
  public CustomResult GetGender(@PathVariable Integer day) {
    return _IChart.TotalGenderBookingByDay(day);
  }

  @GetMapping("byhours")
  @RolesAllowed({ "samedcare" })
  public CustomResult CountBookingByHours() {
    return _IChart.CountBookingByHours();
  }

  @GetMapping("byage/{day}")
  @RolesAllowed({ "samedcare" })
  public CustomResult CountBookingByAge(@PathVariable Integer day) {
    return _IChart.CountBookingByAge(day);
  }

  @GetMapping("dayoffweek/{days}")
  @RolesAllowed({ "samedcare" })
  public CustomResult CountBookingByDayOffWeek(@PathVariable Integer days) {
    return _IChart.countBookingsForWeek(days);
  }

  @GetMapping("tophospital/{days}")
  @RolesAllowed({ "samedcare" })
  public CustomResult CountTopHospital(@PathVariable Integer days) {
    return _IChart.countTopHospital(days);
  }

  @GetMapping("bookinggroupbyType/{days}")
  @RolesAllowed({ "samedcare" })
  public CustomResult BookingGroupByType(@PathVariable Integer days) {
    return _IChart.countBookingByType(days);
  }

}
