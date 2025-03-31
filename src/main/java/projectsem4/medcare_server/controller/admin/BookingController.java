package projectsem4.medcare_server.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IBooking;
import projectsem4.medcare_server.interfaces.admin.IDoctor;
import projectsem4.medcare_server.interfaces.admin.IPack;
import projectsem4.medcare_server.interfaces.admin.ITesting;
import projectsem4.medcare_server.interfaces.admin.IVaccine;
import projectsem4.medcare_server.interfaces.admin.IBooking.FilteAdminHospital;
import projectsem4.medcare_server.interfaces.admin.IBooking.FilterExcel;
import projectsem4.medcare_server.interfaces.admin.IBooking.FilterRes;
import projectsem4.medcare_server.interfaces.admin.IBooking.SearchBookingByHospital;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/admin/booking")
public class BookingController {

  @Autowired
  IBooking _IBooking;

  @PostMapping("getall")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult postMethodName(@RequestBody FilterRes res) {
    return _IBooking.GetAll(res);
  }

  @PostMapping("getbyhospital")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetBookingAdminHospital(@RequestBody FilteAdminHospital res) {
    return _IBooking.GetBookingByType(res);
  }

  @GetMapping("get/{id}")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult getById(@PathVariable Long id) {
    return _IBooking.GetById(id);
  }

  @GetMapping("getservices/{id}")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult GetServicesBooking(@PathVariable Long id) {
    return _IBooking.GetServiceBooking(id);
  }

  @PostMapping("search")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult SearchBooking(@RequestBody SearchBookingByHospital searchfilter) {
    return _IBooking.SearchBooking(searchfilter);
  }

  @PostMapping("excel")
  @RolesAllowed({ "samedcare" })
  public CustomResult getMethodName(@RequestBody FilterExcel res) {
    return _IBooking.GetExcel(res);
  }

}
