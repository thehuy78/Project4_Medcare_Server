package projectsem4.medcare_server.service.customer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Bill;
import projectsem4.medcare_server.domain.entity.Booking;
import projectsem4.medcare_server.domain.entity.Brief;
import projectsem4.medcare_server.domain.entity.Doctor;
import projectsem4.medcare_server.domain.entity.Hospital;
import projectsem4.medcare_server.domain.entity.Notification;
import projectsem4.medcare_server.domain.entity.Pack;
import projectsem4.medcare_server.domain.entity.Test;
import projectsem4.medcare_server.domain.entity.Transaction;
import projectsem4.medcare_server.domain.entity.Type;
import projectsem4.medcare_server.domain.entity.User;
import projectsem4.medcare_server.domain.entity.UserDetail;
import projectsem4.medcare_server.domain.entity.Vaccine;
import projectsem4.medcare_server.interfaces.customer.ICustomerBooking;
import projectsem4.medcare_server.repository.customer.CustomerAuthRepo;
import projectsem4.medcare_server.repository.customer.CustomerBillRepo;
import projectsem4.medcare_server.repository.customer.CustomerBookingRepo;
import projectsem4.medcare_server.repository.customer.CustomerBookingRepo.DoctorDateAndTime;
import projectsem4.medcare_server.repository.customer.CustomerBriefRepo;
import projectsem4.medcare_server.repository.customer.CustomerDoctorRepo;
import projectsem4.medcare_server.repository.customer.CustomerHospitalRepo;
import projectsem4.medcare_server.repository.customer.CustomerLabTestRepo;
import projectsem4.medcare_server.repository.customer.CustomerNotificationRepo;
import projectsem4.medcare_server.repository.customer.CustomerPackageRepo;
import projectsem4.medcare_server.repository.customer.CustomerTransactionRepo;
import projectsem4.medcare_server.repository.customer.CustomerTypeRepo;
import projectsem4.medcare_server.repository.customer.CustomerUserDetailRepo;
import projectsem4.medcare_server.repository.customer.CustomerVaccinationRepo;
import projectsem4.medcare_server.service.EmailService;
import projectsem4.medcare_server.service.FirebaseRealtimeDatabaseService;
import projectsem4.medcare_server.service.FirebaseStorageService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CustomerBookingService implements ICustomerBooking {
    @Autowired
    CustomerBookingRepo _CustomerBookingRepo;
    @Autowired
    CustomerDoctorRepo _CustomerDoctorRepo;
    @Autowired
    CustomerBriefRepo _CustomerBriefRepo;
    @Autowired
    CustomerHospitalRepo _CustomerHospitalRepo;
    @Autowired
    CustomerAuthRepo _CustomerAuthRepo;
    @Autowired
    CustomerPackageRepo _CustomerPackageRepo;
    @Autowired
    CustomerLabTestRepo _CustomerLabTestRepo;
    @Autowired
    CustomerVaccinationRepo _CustomerVaccinationRepo;
    @Autowired
    CustomerUserDetailRepo _CustomerUserDetailRepo;

    @Autowired
    CustomerTransactionRepo _CustomerTransactionRepo;

    @Autowired
    CustomerTypeRepo _CustomerTypeRepo;

    @Autowired
    FirebaseStorageService firebaseStorageService;

    @Autowired
    FirebaseRealtimeDatabaseService _FirebaseRealtimeDatabaseService;

    @Autowired
    CustomerNotificationRepo _CustomerNotificationRepo;

    @Autowired
    EmailService _email;

    @Autowired
    CustomerBillRepo _CustomerBillRepo;

    @Override
    public CustomResult createBooking(BookingDTOCustomer booking) {
        try {
            Booking b = new Booking();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            LocalDateTime localDate = LocalDateTime
                    .parse(booking.getBookingDate().substring(0, booking.getBookingDate().length() - 1), formatter);
            Date date = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
            LocalDateTime currentLocaldate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            Bill bill = new Bill();

            Date currentDate = Date.from(currentLocaldate.atZone(ZoneId.systemDefault()).toInstant());
            if (date.equals(currentDate)) {
                return new CustomResult(203, "over time ", null);
            }

            b.setBookingDate(date);
            b.setBookingTime(booking.getBookingTime());

            if (booking.getDescription() != null) {
                b.setDescription(booking.getDescription());
            }

            Brief br = _CustomerBriefRepo.findById(Long.parseLong(booking.getProfileId())).get();
            User u = br.getUser();
            UserDetail ud = u.getUserDetail();
            Transaction transaction = new Transaction();
            transaction.setBalance(ud.getBalance());
            transaction.setUser(u);
            transaction.setStatus("active");
            Notification notification = new Notification();
            notification.setType("customer:booking");
            notification.setStatus("unread");
            notification.setIsDeleted("undeleted");
            notification.setUser(u);
            bill.setBookingTime(booking.getBookingTime());
            bill.setBookingDate(date);
            bill.setUseremail(u.getEmail());
            if (Long.parseLong(booking.getAttributeId()) == 1 || Long.parseLong(booking.getAttributeId()) == 5) {
                Doctor dc = _CustomerDoctorRepo.findById(Long.parseLong(booking.getDoctorId())).get();
                if (_CustomerBookingRepo.CheckDuplicateBooking(u, br, dc, date, booking.getBookingTime(), null, null,
                        null) != null) {
                    return new CustomResult(210, "Booking Duplicate ", null);
                }
                DoctorDateAndTime DoctorDateAndTime = _CustomerBookingRepo.getCountBookingByDoctorDateAndTimes(dc, date,
                        booking.getBookingTime());
                if (DoctorDateAndTime != null && DoctorDateAndTime.getCountBooking() >= dc.getPatients()) {
                    return new CustomResult(201, "Sold Out ", null);
                }
                b.setDoctor(dc);
                b.setRevenue(dc.getFee());
                b.setProfit(dc.getFee() * 0.1);
                if (booking.getTypePayment().equals("medCare")) {
                    if ((ud.getBalance() >= dc.getFee())) {
                        ud.setBalance(ud.getBalance() - dc.getFee());
                    } else {
                        return new CustomResult(202, "Balance not enought ", null);
                    }
                }
                b.setBrief(br);
                b.setStatus("pending");
                Hospital h = _CustomerHospitalRepo.findById(Long.parseLong(booking.getHospitalId())).get();
                transaction.setTraded(-b.getRevenue());
                b.setHospital(h);
                Type type = _CustomerTypeRepo.findById(Long.parseLong("5")).get();
                b.setUser(br.getUser());
                b.setType(type);

                var qrCodeImageString = createQrCodeImage(booking.getFile());
                b.setQrCodeUrl(qrCodeImageString);
                DoctorDateAndTime DoctorDateAndTime2 = _CustomerBookingRepo.getCountBookingByDoctorDateAndTimes(dc,
                        date,
                        booking.getBookingTime());
                if (DoctorDateAndTime2 != null && DoctorDateAndTime2.getCountBooking() >= dc.getPatients()) {
                    return new CustomResult(201, "Sold Out ", null);
                }

                _CustomerBookingRepo.save(b);
                _CustomerUserDetailRepo.save(ud);
                bill.setService("Booking Doctor");
                bill.setDescription("Doctor:" + dc.getName());
                bill.setFee(dc.getFee());
                bill.setHospitalname(h.getName());

                if (booking.getTypePayment().equals("medCare")) {
                    transaction.setDescription("IdBooking: " + b.getId() + ",Booking Service account Medcare");
                } else {
                    transaction.setDescription("Booking Service account Bank");
                }

                _CustomerTransactionRepo.save(transaction);

                notification.setDescription("Doctor: " + dc.getName());

                _CustomerNotificationRepo.save(notification);

                Long timestamp = System.currentTimeMillis();

                // _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser("booking/randomDoctor",
                // timestamp.toString());
                _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser(
                        "booking/" + booking.getDoctorId().toString(),
                        timestamp.toString());
                _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser("booking/admin/" + h.getCode(),
                        timestamp.toString());
                _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser(
                        "notificationCustomer/" + u.getId().toString(),
                        timestamp.toString());
                insertBill(br, bill);
                var html = "<p>Booking Confirm !</p> </p><p>Thank you for using MedCare's appointment booking service. We are delighted to assist you with your healthcare needs</p>"
                        +
                        "<p>You can access the MedCare app, go to the 'Ticket' section, and scan the QR code to get detailed information about your medical appointment schedule.</p>"
                        +
                        "<p>Wishing you good health</p>" +
                        "<p>The MedCare Team</p>";
                _email.sendEmail(u.getEmail(), "Medcare Booking Confirm", html);
                return new CustomResult(200, "Success", ud.getBalance().toString());
            }
            if (Long.parseLong(booking.getAttributeId()) == 2) {
                Pack pc = _CustomerPackageRepo.findById(Long.parseLong(booking.getPackId())).get();
                if (_CustomerBookingRepo.CheckDuplicateBooking(u, br, null, date, booking.getBookingTime(), pc, null,
                        null) != null) {
                    return new CustomResult(210, "Booking Duplicate ", null);
                }
                b.setPack(pc);
                b.setRevenue(pc.getFee());
                b.setProfit(pc.getFee() * 0.1);
                if ((ud.getBalance() >= pc.getFee())) {
                    ud.setBalance(ud.getBalance() - pc.getFee());
                } else {
                    return new CustomResult(202, "Balance not enought ", null);
                }
                Type type = _CustomerTypeRepo.findById(Long.parseLong("6")).get();
                b.setType(type);

                notification.setDescription("Health Care: " + pc.getName());
                bill.setService("Booking Health Care");
                bill.setDescription("Pack Name:" + pc.getName());
                bill.setFee(pc.getFee());

            }
            if (Long.parseLong(booking.getAttributeId()) == 3) {
                Test pc = _CustomerLabTestRepo.findById(Long.parseLong(booking.getPackId())).get();
                if (_CustomerBookingRepo.CheckDuplicateBooking(u, br, null, date, booking.getBookingTime(), null, pc,
                        null) != null) {
                    return new CustomResult(210, "Booking Duplicate ", null);
                }
                b.setTest(pc);
                b.setRevenue(pc.getFee());
                b.setProfit(pc.getFee() * 0.1);
                if ((ud.getBalance() >= pc.getFee())) {
                    ud.setBalance(ud.getBalance() - pc.getFee());
                } else {
                    return new CustomResult(202, "Balance not enought ", null);
                }
                Type type = _CustomerTypeRepo.findById(Long.parseLong("7")).get();
                b.setType(type);
                notification.setDescription("Lab Test: " + pc.getName());
                bill.setService("Booking Lab Test");
                bill.setDescription("Pack Name:" + pc.getName());
                bill.setFee(pc.getFee());
            }
            if (Long.parseLong(booking.getAttributeId()) == 4) {
                Vaccine pc = _CustomerVaccinationRepo.findById(Long.parseLong(booking.getPackId())).get();
                if (_CustomerBookingRepo.CheckDuplicateBooking(u, br, null, date, booking.getBookingTime(), null, null,
                        pc) != null) {
                    return new CustomResult(210, "Booking Duplicate ", null);
                }
                b.setVaccine(pc);
                b.setRevenue(pc.getFee());
                b.setProfit(pc.getFee() * 0.1);
                if ((ud.getBalance() >= pc.getFee())) {
                    ud.setBalance(ud.getBalance() - pc.getFee());
                } else {
                    return new CustomResult(202, "Balance not enought ", null);
                }

                Type type = _CustomerTypeRepo.findById(Long.parseLong("8")).get();
                b.setType(type);
                notification.setDescription("Vaccine: " + pc.getName());
                bill.setService("Booking Vaccine");
                bill.setDescription("Pack Name:" + pc.getName());
                bill.setFee(pc.getFee());
            }
            b.setBrief(br);
            b.setStatus("pending");
            Hospital h = _CustomerHospitalRepo.findById(Long.parseLong(booking.getHospitalId())).get();
            transaction.setTraded(-b.getRevenue());
            b.setHospital(h);

            b.setUser(br.getUser());

            var qrCodeImageString = createQrCodeImage(booking.getFile());
            b.setQrCodeUrl(qrCodeImageString);

            _CustomerBookingRepo.save(b);
            _CustomerUserDetailRepo.save(ud);
            _CustomerNotificationRepo.save(notification);

            if (booking.getTypePayment().equals("medCare")) {
                transaction.setDescription("IdBooking: " + b.getId() + ",Booking Service account Medcare");
            } else {
                transaction.setDescription("Booking Service account Bank");
            }

            _CustomerTransactionRepo.save(transaction);

            Long timestamp = System.currentTimeMillis();

            _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser("booking/" + booking.getPackId().toString(),
                    timestamp.toString());
            _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser("booking/admin/" + h.getCode(),
                    timestamp.toString());
            _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser(
                    "notificationCustomer",
                    timestamp.toString());
            bill.setHospitalname(h.getName());
            insertBill(br, bill);
            var html = "<p>Booking Confirm !</p> </p><p>Thank you for using MedCare's appointment booking service. We are delighted to assist you with your healthcare needs</p>"
                    +
                    "<p>You can access the MedCare app, go to the 'Ticket' section, and scan the QR code to get detailed information about your medical appointment schedule.</p>"
                    +
                    "<p>Wishing you good health</p>" +
                    "<p>The MedCare Team</p>";
            _email.sendEmail(u.getEmail(), "Medcare Booking Confirm", html);
            return new CustomResult(200, "Success", ud.getBalance().toString());
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult getByUserId(String id) {
        try {
            User u = _CustomerAuthRepo.findById(Long.parseLong(id)).get();
            return new CustomResult(200, "Success", _CustomerBookingRepo.findByUser(u));
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    public String createQrCodeImage(MultipartFile file) {
        try {
            var result = firebaseStorageService.uploadFile(file);
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public CustomResult getByDoctorDateAndTime(String id, String dateString) {
        try {
            Doctor doctor = _CustomerDoctorRepo.findById(Long.parseLong(id)).get();
            Date date = null;
            if (dateString != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                LocalDateTime localDate = LocalDateTime
                        .parse(dateString.substring(0,
                                dateString.length() - 1), formatter);
                date = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
            }

            return new CustomResult(200, "Success", _CustomerBookingRepo.getByDoctorDateAndTimes(doctor, date));
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    public void insertBill(Brief br, Bill b) {
        b.setPatientname(br.getName());
        b.setAddress(br.getAddress());
        b.setDistrict(br.getDistrict());
        b.setWard(br.getWard());
        b.setAddress(br.getAddress());
        b.setDob(br.getDob());
        b.setGender(br.getGender());
        b.setPhone(br.getPhone());
        b.setProvince(br.getProvince());
        b.setIdentifier(br.getIdentifier());
        b.setJob(br.getJob());
        _CustomerBillRepo.save(b);
    }
}
