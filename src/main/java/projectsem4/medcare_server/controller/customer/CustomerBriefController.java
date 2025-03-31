package projectsem4.medcare_server.controller.customer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.customer.ICustomerBrief;
import projectsem4.medcare_server.interfaces.customer.ICustomerBrief.CustomerCreateBriefDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/customer/brief")
public class CustomerBriefController {

    @Autowired
    ICustomerBrief _ICustomerBrief;

    @PostMapping("create")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> create(@ModelAttribute CustomerCreateBriefDTO customerCreateBriefDTO) {
        var result = _ICustomerBrief.create(customerCreateBriefDTO);
        if (result.getStatus() == 200 || result.getStatus() == 201 || result.getStatus() == 202) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getByUserId/{userid}")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> getMethodName(@PathVariable String userid) {
        var result = _ICustomerBrief.getByUserId(userid);
        if (result.getStatus() == 200 || result.getStatus() == 201) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("delete/{id}")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> delete(@PathVariable String id) {
        var result = _ICustomerBrief.delete(id);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getDetail/{id}")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> getDetail(@PathVariable String id) {
        var result = _ICustomerBrief.getDetail(id);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

}
