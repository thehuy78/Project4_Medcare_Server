package projectsem4.medcare_server.interfaces.customer;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface ICustomerBooking {
    CustomResult createBooking(BookingDTOCustomer b);

    CustomResult getByUserId(String id);

    CustomResult getByDoctorDateAndTime(String id, String date);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class BookingDTOCustomer {
        String profileId;
        String bookingDate;
        String bookingTime;
        String description;
        String doctorId;
        String hospitalId;
        String packId;
        String attributeId;
        String typePayment;
        MultipartFile file;
    }
}
