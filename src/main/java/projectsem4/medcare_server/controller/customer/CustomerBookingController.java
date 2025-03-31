package projectsem4.medcare_server.controller.customer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.customer.ICustomerBooking;
import projectsem4.medcare_server.interfaces.customer.ICustomerBooking.BookingDTOCustomer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/customer/booking")
public class CustomerBookingController {
    @Autowired
    ICustomerBooking _ICustomerBooking;

    @PostMapping("create")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> create(@ModelAttribute BookingDTOCustomer bk) {
        var result = _ICustomerBooking.createBooking(bk);
        if (result.getStatus() == 200 || result.getStatus() == 201 || result.getStatus() == 202
                || result.getStatus() == 203 || result.getStatus() == 210) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getByUserId/{id}")
    @RolesAllowed({ "customer" })
    public ResponseEntity<CustomResult> getByUserId(@PathVariable String id) {
        var result = _ICustomerBooking.getByUserId(id);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("getByDoctorDateAndTime")
    public ResponseEntity<CustomResult> getByDoctorDateAndTime(@RequestParam String id,
            @RequestParam(required = false) String date) {
        var result = _ICustomerBooking.getByDoctorDateAndTime(id, date);
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

}
