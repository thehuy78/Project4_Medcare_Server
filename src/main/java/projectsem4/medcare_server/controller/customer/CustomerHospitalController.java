package projectsem4.medcare_server.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.customer.ICustomerHospital;

@RestController
@RequestMapping("api/customer/hospital")

public class CustomerHospitalController {

    @Autowired
    ICustomerHospital _ICustomerHospital;

    @GetMapping("getBestPartner")

    public ResponseEntity<CustomResult> getBestPartner() {
        var result = _ICustomerHospital.GetBestPartner();
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getHospitalSuggestion")
    public ResponseEntity<CustomResult> getHospitalSuggestion() {
        var result = _ICustomerHospital.getHospitalSuggestion();
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getBestFacility")
    public ResponseEntity<CustomResult> getBestFacility() {
        var result = _ICustomerHospital.GetBestFacility();
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("detailHospital/{id}")
    public ResponseEntity<CustomResult> detailHospital(@PathVariable String id) {
        var result = _ICustomerHospital.GetDetailFacility(id);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getHospitalService/{id}")
    public ResponseEntity<CustomResult> getHospitalService(@PathVariable String id) {
        var result = _ICustomerHospital.GetServiceByHospital(id);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getAll")
    public ResponseEntity<CustomResult> getAll(@RequestParam String pageNo,
            @RequestParam(required = false) String searchValue, @RequestParam String typeId) {
        var result = _ICustomerHospital.GetAll(Integer.parseInt(pageNo), searchValue, Integer.parseInt(typeId));
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("searchAllHospital")
    public ResponseEntity<CustomResult> searchAllHospital(@RequestParam String pageNo,
            @RequestParam(required = false) String searchValue) {
        var result = _ICustomerHospital.searchAllHospital(Integer.parseInt(pageNo), searchValue);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getTypeHospital")
    public ResponseEntity<CustomResult> getTypeHospital() {
        var result = _ICustomerHospital.getTypeHospital();
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

}
