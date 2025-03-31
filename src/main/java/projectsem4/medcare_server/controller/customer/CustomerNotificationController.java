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
import projectsem4.medcare_server.interfaces.customer.ICustomerNotification;

@RestController
@RequestMapping("api/customer/notification")
public class CustomerNotificationController {
    @Autowired
    ICustomerNotification _ICustomerNotification;

    @GetMapping("getSeenByUser/{id}")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> getSeenByUser(@PathVariable String id) {
        var result = _ICustomerNotification.getSeenByUser(id);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getLazy")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> getLazy(@RequestParam String id, @RequestParam String pageNo,
            @RequestParam String typeValue) {
        var result = _ICustomerNotification.getLazy(id, pageNo, typeValue);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("deleteNotification/{id}")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> deleteNotification(@PathVariable String id) {
        var result = _ICustomerNotification.deleteNotification(id);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("seenNotification/{id}")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> seenNotification(@PathVariable String id) {
        var result = _ICustomerNotification.seenNotification(id);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

}
