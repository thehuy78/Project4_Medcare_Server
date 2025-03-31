package projectsem4.medcare_server.service.admin;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Booking;
import projectsem4.medcare_server.interfaces.admin.IBooking;
import projectsem4.medcare_server.interfaces.admin.IDoctor;
import projectsem4.medcare_server.interfaces.admin.IPack;
import projectsem4.medcare_server.interfaces.admin.ITesting;
import projectsem4.medcare_server.interfaces.admin.IVaccine;
import projectsem4.medcare_server.repository.admin.BookingRepository;

@Service
public class BookingService implements IBooking {

  @Autowired
  BookingRepository bookingRepo;

  @Autowired
  IDoctor _IDoctor;
  @Autowired
  IPack _IPack;
  @Autowired
  ITesting _ITesting;
  @Autowired
  IVaccine _IVaccine;

  @Override
  public CustomResult GetAll(FilterRes filterRes) {
    try {
      int page = filterRes.getPage() != null ? filterRes.getPage() : 0;
      int size = filterRes.getSize() != null ? filterRes.getSize() : 10;
      Double revenueStart = null;
      Double revenueEnd = null;
      Date startDate = filterRes.getStartDate(); // Get start date
      Date endDate = filterRes.getEndDate(); // Get end date

      if (filterRes.getRevenue() != null && filterRes.getRevenue().size() == 2) {
        revenueStart = filterRes.getRevenue().get(0); // Kiểm tra xem có phần tử đầu tiên không
        revenueEnd = filterRes.getRevenue().get(1); // Kiểm tra xem có phần tử thứ hai không
      } else {
        revenueStart = null; // Nếu không đủ phần tử, đặt về null
        revenueEnd = null; // Nếu không đủ phần tử, đặt về null
      }
      Page<BookingRes> bookingsPage;
      switch (filterRes.getTypeUrl()) {
        case "":
          bookingsPage = bookingRepo.findByFilters(
              filterRes.getSearch(),
              filterRes.getGender(),
              filterRes.getType(),
              filterRes.getHospitalCode(),
              revenueStart, // Truyền feeStart
              revenueEnd, // Truyền feeEnd
              startDate, // Pass start date
              endDate, // Pass end date
              PageRequest.of(page, size));
          break;
        case "doctor":
          bookingsPage = bookingRepo.findByFiltersDoctor(
              filterRes.getSearch(),
              filterRes.getGender(),
              filterRes.getType(),
              filterRes.getTypeUrl(),
              filterRes.getIdUrl(),
              revenueStart, // Truyền feeStart
              revenueEnd, // Truyền feeEnd
              startDate, // Pass start date
              endDate, // Pass end date
              PageRequest.of(page, size));
          break;
        case "package":
          bookingsPage = bookingRepo.findByFiltersPack(
              filterRes.getSearch(),
              filterRes.getGender(),
              filterRes.getType(),
              filterRes.getTypeUrl(),
              filterRes.getIdUrl(),
              revenueStart, // Truyền feeStart
              revenueEnd, // Truyền feeEnd
              startDate, // Pass start date
              endDate, // Pass end date
              PageRequest.of(page, size));
          break;
        case "testing":
          bookingsPage = bookingRepo.findByFiltersTest(
              filterRes.getSearch(),
              filterRes.getGender(),
              filterRes.getType(),
              filterRes.getTypeUrl(),
              filterRes.getIdUrl(),
              revenueStart, // Truyền feeStart
              revenueEnd, // Truyền feeEnd
              startDate, // Pass start date
              endDate, // Pass end date
              PageRequest.of(page, size));
          break;
        case "vaccine":
          bookingsPage = bookingRepo.findByFiltersVaccine(
              filterRes.getSearch(),
              filterRes.getGender(),
              filterRes.getType(),
              filterRes.getTypeUrl(),
              filterRes.getIdUrl(),
              revenueStart, // Truyền feeStart
              revenueEnd, // Truyền feeEnd
              startDate, // Pass start date
              endDate, // Pass end date
              PageRequest.of(page, size));
          break;

        default:
          return new CustomResult(400, "error", null);
      }
      return new CustomResult(200, "success", bookingsPage);

    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult GetById(Long id) {
    try {
      var rs = bookingRepo.findById(id);
      if (rs.isPresent()) {
        BookingDetailsRes res = new BookingDetailsRes();
        Booking b = rs.get();
        res.setId(b.getId());
        res.setName(b.getBrief().getName());
        res.setDob(b.getBrief().getDob());
        res.setGender(b.getBrief().getGender());
        res.setAddress(b.getBrief().getAddress());
        res.setProvince(b.getBrief().getProvince());
        res.setDistrict(b.getBrief().getDistrict());
        res.setWard(b.getBrief().getWard());
        res.setIdentifier(b.getBrief().getIdentifier());
        res.setJob(b.getBrief().getJob());
        res.setHospitalName(b.getHospital().getName());
        res.setHospitalCode(b.getHospital().getCode());
        res.setHospitalLogo(b.getHospital().getLogo());
        if (b.getDoctor() != null) {
          res.setDoctorLevel(b.getDoctor().getLevel());
          res.setDoctorCode(b.getDoctor().getCode());
          res.setDoctorName(b.getDoctor().getName());
        }
        if (b.getPack() != null) {
          res.setPackName(b.getPack().getName());
          res.setPackCode(b.getPack().getCode());
        }
        if (b.getTest() != null) {
          res.setTestCode(b.getTest().getCode());
          res.setTestName(b.getTest().getName());
        }
        if (b.getVaccine() != null) {
          res.setVaccineCode(b.getVaccine().getCode());
          res.setVaccineName(b.getVaccine().getName());
        }
        res.setScheduleDate(b.getBookingDate());
        res.setScheduleTime(b.getBookingTime());
        res.setStatus(b.getStatus());
        res.setCreateDate(b.getCreateDate());
        res.setEmail(b.getUser().getEmail());
        res.setFirstName(b.getUser().getUserDetail().getFirstName());
        res.setLastName(b.getUser().getUserDetail().getLastName());
        res.setVerify(b.getUser().getUserDetail().getVerify());
        res.setProfit(b.getProfit());
        res.setRevenue(b.getRevenue());

        return new CustomResult(200, "Get success", res);
      }
      return new CustomResult(300, "Booking not empty", null);

    } catch (Exception e) {
      return new CustomResult(400, "Booking not empty", e.getMessage());
    }
  }

  @Override
  public CustomResult GetExcel(FilterExcel res) {
    var rs = bookingRepo.findBookingsWithFilter(
        res.getType().isEmpty() ? null : res.getType(),
        res.getStartDate(),
        res.getEndDate());
    return new CustomResult(200, "ok", rs);
  }

  @Override
  public CustomResult GetBookingByType(FilteAdminHospital filterRes) {
    try {
      int page = filterRes.getPage() != null ? filterRes.getPage() : 0;
      int size = filterRes.getSize() != null ? filterRes.getSize() : 10;

      Date startDate = filterRes.getStartDate(); // Get start date
      Date endDate = filterRes.getEndDate(); // Get end date
      if (startDate != null) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        setToStartOfDay(cal);
        startDate = cal.getTime();
      }

      if (endDate != null) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        setToEndOfDay(cal);
        endDate = cal.getTime();
      }
      Page<BookingRes> bookingsPage = bookingRepo.findByHospitalType(
          filterRes.getSearch(),
          filterRes.getType(),
          filterRes.getHospitalCode(),
          startDate, // Pass start date
          endDate, // Pass end date
          PageRequest.of(page, size));

      return new CustomResult(200, "success", bookingsPage);

    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult SearchBooking(SearchBookingByHospital filter) {
    try {
      int page = filter.getPage() != null ? filter.getPage() : 0;
      int size = filter.getSize() != null ? filter.getSize() : 10;
      Page<BookingRes> bookingsPage = bookingRepo.searchBooking(
          filter.getSearch(),
          filter.getHospitalCode(),
          PageRequest.of(page, size));

      return new CustomResult(200, "success", bookingsPage);

    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult GetServiceBooking(Long id) {
    try {
      var isBooking = bookingRepo.findById(id);
      if (isBooking.isPresent()) {
        Booking booking = isBooking.get();
        String type = booking.getType().getName();
        switch (type.toLowerCase()) {
          case "doctor":
            return _IDoctor.GetById(booking.getDoctor().getId());
          case "package":
            return _IPack.GetById(booking.getPack().getId());
          case "testing":
            return _ITesting.GetById(booking.getTest().getId());
          case "vaccine":
            return _IVaccine.GetById(booking.getVaccine().getId());
          default:
            return new CustomResult(300, "Booking is empty", null);
        }
      }
      return new CustomResult(300, "Booking is empty", null);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  private void setToStartOfDay(Calendar cal) {
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
  }

  private void setToEndOfDay(Calendar cal) {
    cal.set(Calendar.HOUR_OF_DAY, 23);
   

    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 999);
  }
  @Scheduled(cron = "0 0 01 * * ?") // bat dau tu 0s 0p 23h moi ngay, moi thang, k xac dinh tuan
  @Transactional
  public void changeStatusBooking() {
    Date now = new Date();
    // Lấy ngày hiện tại mà không quan tâm đến giờ (cắt giờ, phút, giây)
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(now);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    Date today = calendar.getTime(); // Ngày hiện tại (không có giờ, phút, giây)
    // Lấy tất cả các booking
    List<Booking> bookings = bookingRepo.findAll();
    if (!bookings.isEmpty()) {
      bookings.forEach(booking -> {
        // Kiểm tra nếu bookingDate trước ngày hiện tại và trạng thái là "pending"
        if (booking.getBookingDate().before(today) && "pending".equals(booking.getStatus())) {
          // Cập nhật trạng thái thành "cancel"
          booking.setStatus("cancel");
          bookingRepo.save(booking); // Lưu booking sau khi thay đổi trạng thái
        }
      });
    }
  }

}
