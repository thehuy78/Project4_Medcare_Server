package projectsem4.medcare_server.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projectsem4.medcare_server.domain.dto.CustomResult;

import projectsem4.medcare_server.interfaces.customer.ICustomerPackage;

@RestController
@RequestMapping("api/customer/package")
public class CustomerPackageController {
    @Autowired
    ICustomerPackage _ICustomerPackage;

    @GetMapping("getBestPackageHealthCare")
    public ResponseEntity<CustomResult> getBestPackageHealthCare() {
        var result = _ICustomerPackage.GetBestPackageHealthCare();
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getBestPackageLabTest")
    public ResponseEntity<CustomResult> getBestPackageLabTest() {
        var result = _ICustomerPackage.GetBestPackageLabTest();
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getBestPackageVaccination")
    public ResponseEntity<CustomResult> getBestPackageVaccination() {
        var result = _ICustomerPackage.GetBestPackageVaccination();
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getDetailPackage/{attributeId}/{id}")
    public ResponseEntity<CustomResult> getDetailPackage(@PathVariable String attributeId, @PathVariable String id) {
        var result = _ICustomerPackage.GetDetailPackage(attributeId, id);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getPackageByHospital")
    public ResponseEntity<CustomResult> getPackageHealthCareByHospital(
            @RequestParam(required = false) String hospitalId,
            @RequestParam String pageNo, @RequestParam String attributeId,
            @RequestParam(required = false) String searchValue,
            @RequestParam(required = false) String filterHospitalName) {
        var result = _ICustomerPackage.GetPackageByHospital(hospitalId, Integer.parseInt(pageNo),
                Integer.parseInt(attributeId), searchValue, filterHospitalName);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getPackageSuggestion")
    public ResponseEntity<CustomResult> getPackageSuggestion(
            @RequestParam(required = false) String hospitalId,
            @RequestParam String attributeId) {
        var result = _ICustomerPackage.getPackageSuggestion(hospitalId, Integer.parseInt(attributeId));
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("searchAllPack")
    public ResponseEntity<CustomResult> searchAllPack(
            @RequestParam String pageNo,
            @RequestParam(required = false) String searchValue) {
        var result = _ICustomerPackage.searchAllPack(Integer.parseInt(pageNo), searchValue);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

}
