package projectsem4.medcare_server.controller.customer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.customer.ICustomerDepartment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/customer/department")
public class CustomerDepartmentController {
    @Autowired
    ICustomerDepartment _ICustomerDepartment;

    @GetMapping("getDepartment/{id}")
    public ResponseEntity<CustomResult> getDepartment(@PathVariable String id) {
        var result = _ICustomerDepartment.getByHospitalId(id);
        if (result.getStatus() == 200 || result.getStatus() == 205 || result.getStatus() == 206
                || result.getStatus() == 207) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getAll")
    public ResponseEntity<CustomResult> getAll() {
        var result = _ICustomerDepartment.GetAll();
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getDepartmentBySearchAll")
    public ResponseEntity<CustomResult> getDepartmentBySearchAll(
            @RequestParam String pageNo,
            @RequestParam(required = false) String searchValue) {
        var result = _ICustomerDepartment.getDepartmentBySearchAll(Integer.parseInt(pageNo), searchValue);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

}
