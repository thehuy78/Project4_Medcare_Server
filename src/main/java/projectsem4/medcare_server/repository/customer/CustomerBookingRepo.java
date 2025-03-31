package projectsem4.medcare_server.repository.customer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.entity.Booking;
import projectsem4.medcare_server.domain.entity.Brief;
import projectsem4.medcare_server.domain.entity.Doctor;
import projectsem4.medcare_server.domain.entity.Pack;
import projectsem4.medcare_server.domain.entity.Test;
import projectsem4.medcare_server.domain.entity.User;
import projectsem4.medcare_server.domain.entity.Vaccine;
import projectsem4.medcare_server.repository.customer.CustomerHospitalRepo.BestFacitlityDTO;

import java.util.Date;
import java.util.List;

public interface CustomerBookingRepo extends JpaRepository<Booking, Long> {

        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerBookingRepo$BookingGetDTO(" +
                        "b.id, b.bookingDate, b.bookingTime,b.qrCodeUrl, b.revenue, h.name, br.name, " +
                        "CASE WHEN b.doctor IS NOT NULL THEN d.name ELSE NULL END, " +
                        "CASE " +
                        "  WHEN b.pack IS NOT NULL THEN pc.name " +
                        "  WHEN b.test IS NOT NULL THEN t.name " +
                        "  ELSE vc.name " +
                        "END) " +
                        "FROM Booking b " +
                        "LEFT JOIN b.hospital h " +
                        "LEFT JOIN b.doctor d " +
                        "LEFT JOIN b.pack pc " +
                        "LEFT JOIN b.test t " +
                        "LEFT JOIN b.vaccine vc " +
                        "JOIN b.brief br " +
                        "WHERE b.user = :user AND b.status = 'pending' " +
                        "ORDER BY b.bookingDate , b.bookingTime ")
        List<BookingGetDTO> findByUser(@Param("user") User user);

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public class BookingGetDTO {
                Long id;
                Date bookingDate;
                String bookingTime;
                String qrCodeUrl;
                Double fee;
                String hospitalName;
                String patientName;
                String doctorName;
                String packName;
        }

        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerBookingRepo$DoctorDateAndTime(" +
                        "b.bookingTime,b.bookingDate, d.patients ,COUNT(b.bookingTime))" +
                        "FROM Booking b join b.doctor d " +
                        "WHERE b.doctor = :doctor AND (:bookingDate IS NULL OR b.bookingDate =:bookingDate) " +
                        "GROUP BY b.bookingTime ,b.bookingDate " +
                        "ORDER BY MAX(b.createDate) DESC, b.bookingTime DESC")
        List<DoctorDateAndTime> getByDoctorDateAndTimes(@Param("doctor") Doctor doctor,
                        @Param("bookingDate") Date bookingDate);

        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerBookingRepo$DoctorDateAndTime(" +
                        "b.bookingTime,b.bookingDate, d.patients ,COUNT(b.bookingTime))" +
                        "FROM Booking b join b.doctor d " +
                        "WHERE b.doctor = :doctor AND b.bookingTime =:bookingTime AND  b.bookingDate =:bookingDate AND b.status = 'pending' "
                        +
                        "GROUP BY b.bookingTime ,b.bookingDate " +
                        "ORDER BY MAX(b.createDate) DESC, b.bookingTime DESC ")
        DoctorDateAndTime getCountBookingByDoctorDateAndTimes(@Param("doctor") Doctor doctor,
                        @Param("bookingDate") Date bookingDate, @Param("bookingTime") String bookingTime);

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class DoctorDateAndTime {
                String bookingTime;
                Date bookingDate;
                Integer patients;
                Long countBooking;
        }

        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerBookingRepo$BestFacitlityDTO(" +
                        "b.hospital.id, b.hospital.name, b.hospital.address, b.hospital.image, " +
                        "CASE WHEN b.hospital.province LIKE '%Thành phố%' THEN REPLACE(b.hospital.province, 'Thành phố ', 'TP.') "
                        +
                        "ELSE b.hospital.province END, " +
                        "CASE WHEN b.hospital.district LIKE '%Quận%' THEN REPLACE(b.hospital.district, 'Quận ', 'Q.') "
                        +
                        "WHEN b.hospital.district LIKE '%Thành Phố%' THEN REPLACE(b.hospital.district, 'Thành Phố ', 'TP.') "
                        +
                        "ELSE b.hospital.district END) " +
                        "FROM Booking b " +
                        "WHERE b.hospital.status = 'active' " +
                        "GROUP BY b.hospital.id, b.hospital.name, b.hospital.address, b.hospital.image, b.hospital.province, b.hospital.district "
                        +
                        "ORDER BY COUNT(b) DESC LIMIT 5")
        List<BestFacitlityDTO> GetBestFacility();

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class BestFacitlityDTO {
                private Long id;
                private String name;
                private String address;
                private String logo;
                private String province;
                private String district;

        }
        
         @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerBookingRepo$BookingGetDTO(" +
                        "b.id, b.bookingDate, b.bookingTime,b.qrCodeUrl, b.revenue, h.name, br.name, " +
                        "CASE WHEN b.doctor IS NOT NULL THEN d.name ELSE NULL END, " +
                        "CASE " +
                        "  WHEN b.pack IS NOT NULL THEN pc.name " +
                        "  WHEN b.test IS NOT NULL THEN t.name " +
                        "  ELSE vc.name " +
                        "END) " +
                        "FROM Booking b " +
                        "LEFT JOIN b.hospital h " +
                        "LEFT JOIN b.doctor d " +
                        "LEFT JOIN b.pack pc " +
                        "LEFT JOIN b.test t " +
                        "LEFT JOIN b.vaccine vc " +
                        "JOIN b.brief br " +
                        "WHERE b.user = :user AND b.brief =:brief AND b.status = 'pending' AND ( doctor IS NULL OR b.doctor =:doctor) AND (Pack IS NULL OR b.pack =:Pack) AND (Test IS NULL OR b.test =:Test) AND (Vaccine IS NULL OR b.vaccine =:Vaccine)  AND b.bookingDate =:bookingdate AND b.bookingTime =:bookingtime "
                        +
                        "ORDER BY b.bookingDate , b.bookingTime ")
        BookingGetDTO CheckDuplicateBooking(@Param("user") User user, @Param("brief") Brief brief,
                        @Param("doctor") Doctor doctor,
                        @Param("bookingdate") Date bookingdate, @Param("bookingtime") String bookingtime,
                        @Param("Pack") Pack Pack, @Param("Test") Test Test, @Param("Vaccine") Vaccine Vaccine);
}
