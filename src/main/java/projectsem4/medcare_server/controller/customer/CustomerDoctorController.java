package projectsem4.medcare_server.controller.customer;

import org.springframework.web.bind.annotation.RestController;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.customer.ICustomerDoctor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/customer/doctor")
public class CustomerDoctorController {

    @Autowired
    ICustomerDoctor _ICustomerDoctor;

    @GetMapping("getByDepartmentId/{id}/{date}")
    public ResponseEntity<CustomResult> getByDepartmentId(@PathVariable String id, @PathVariable String date) {
        var result = _ICustomerDoctor.getByDepartMentId(id, date);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getDoctorByHospitalIdFilter")
    public ResponseEntity<CustomResult> getDoctorByHospitalIdFilter(@RequestParam(required = false) String hospitalId,
            @RequestParam String pageNo,
            @RequestParam(required = false) String searchValue, @RequestParam(required = false) String filterOption) {
        var result = _ICustomerDoctor.getByHospitalFilter(hospitalId, Integer.parseInt(pageNo), searchValue,
                filterOption);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getDoctorBySearchAll")
    public ResponseEntity<CustomResult> getDoctorBySearchAll(
            @RequestParam String pageNo,
            @RequestParam(required = false) String searchValue) {
        var result = _ICustomerDoctor.getDoctorBySearchAll(Integer.parseInt(pageNo), searchValue);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // id null thì trả về tất cả
    @GetMapping("getByHospitalId")
    public ResponseEntity<CustomResult> getByHospitalId(@RequestParam(required = false) String hospitalId) {
        var result = _ICustomerDoctor.getByHospitalLite(hospitalId);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getOfflineByDoctorID/{id}")
    public ResponseEntity<CustomResult> getOfflineByDoctorID(@PathVariable String id) {
        var result = _ICustomerDoctor.getOfflineDay(id);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

}
