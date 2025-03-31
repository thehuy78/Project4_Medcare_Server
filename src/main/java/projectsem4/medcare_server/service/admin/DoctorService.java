package projectsem4.medcare_server.service.admin;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Department;
import projectsem4.medcare_server.domain.entity.Doctor;
import projectsem4.medcare_server.domain.entity.Test;
import projectsem4.medcare_server.interfaces.admin.IDepartment.DepartmentRes;
import projectsem4.medcare_server.repository.admin.BookingRepository;
import projectsem4.medcare_server.repository.admin.DepartmentRepository;
import projectsem4.medcare_server.repository.admin.DoctorRepository;
import projectsem4.medcare_server.repository.admin.HospitalRepository;
import projectsem4.medcare_server.repository.admin.UserRepository;
import projectsem4.medcare_server.service.FirebaseStorageService;
import projectsem4.medcare_server.interfaces.admin.IDoctor;

@Service
public class DoctorService implements IDoctor {

  @Autowired
  DoctorRepository _doctorRepo;

  @Autowired
  HospitalRepository _hospitallRepo;

  @Autowired
  UserRepository _userRepo;

  @Autowired
  BookingRepository _bookingRepo;

  @Autowired
  DepartmentRepository _departmentRepo;

  @Autowired
  FirebaseStorageService firebaseStorageService;

  @Override
  public CustomResult GetAll(FilterRes filterRes) {
    try {
      int page = filterRes.getPage() != null ? filterRes.getPage() : 0;
      int size = filterRes.getSize() != null ? filterRes.getSize() : 10;
      Integer feeStart = null;
      Integer feeEnd = null;

      if (filterRes.getFee() != null && filterRes.getFee().size() == 2) {
        feeStart = filterRes.getFee().get(0); // Kiểm tra xem có phần tử đầu tiên không
        feeEnd = filterRes.getFee().get(1); // Kiểm tra xem có phần tử thứ hai không
      } else {
        feeStart = null; // Nếu không đủ phần tử, đặt về null
        feeEnd = null; // Nếu không đủ phần tử, đặt về null
      }

      Page<DoctorRes> doctorsPage = _doctorRepo.findByFilters(
          filterRes.getCodehospital(),
          filterRes.getCodedepartment(),
          filterRes.getGender(),
          filterRes.getLevel(),
          filterRes.getSearch(),
          feeStart, // Truyền feeStart
          feeEnd, // Truyền feeEnd
          filterRes.getTimeWork(),
          PageRequest.of(page, size));
      return new CustomResult(200, "success", doctorsPage);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult GetById(Long id) {
    try {
      var isDoctor = _doctorRepo.findById(id);
      if (isDoctor.isPresent()) {
        Doctor doctor = isDoctor.get();
        DoctorRes doctorRes = new DoctorRes();
        BeanUtils.copyProperties(doctor, doctorRes);
        doctorRes.setDepartmentCode(doctor.getDepartment().getCode());
        doctorRes.setDepartmentName(doctor.getDepartment().getName());
        doctorRes.setHospitalCode(doctor.getHospital().getCode());
        doctorRes.setHospitalName(doctor.getHospital().getName());
        doctorRes.setDepartmentId(doctor.getDepartment().getId());
        doctorRes.setUserId(doctor.getUser().getId());
        return new CustomResult(200, "success", doctorRes);
      }
      return new CustomResult(300, "Doctor is empty", null);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult Create(DoctorDto dto) {
    try {
      var isDoctor = _doctorRepo.findByCode(dto.getCode());
      if (isDoctor.size() > 0) {
        return new CustomResult(300, "Duplicate code", null);
      }
      var isHospital = _hospitallRepo.findById(dto.getHospitalId());
      var isDepartment = _departmentRepo.findById(dto.getDepartmentId());
      var isUser = _userRepo.findById(dto.getUserId());
      if (!isHospital.isPresent()) {
        return new CustomResult(300, "Hospital is not exist", null);
      }
      if (!isDepartment.isPresent()) {
        return new CustomResult(300, "Department is not exist", null);
      }
      if (!isUser.isPresent()) {
        return new CustomResult(300, "User is not exist", null);
      }
      Doctor doctor = new Doctor();
      doctor.setCode(dto.getCode());
      doctor.setDepartment(isDepartment.get());
      doctor.setHospital(isHospital.get());
      doctor.setUser(isUser.get());
      doctor.setFee(dto.getFee());
      doctor.setGender(dto.getGender());
      doctor.setLevel(dto.getLevel());
      doctor.setName(dto.getName());
      doctor.setPatients(dto.getPatients());
      doctor.setRoom(dto.getRoom());

      doctor.setStatus("active");
      doctor.setTimeWork(dto.getTimeWork());
      doctor.setWorkDay(dto.getWorkDay());
      if (dto.getAvatar() != null && !dto.getAvatar().isEmpty()) {
        doctor.setAvatar(firebaseStorageService.uploadFile(dto.getAvatar()));
      } else {
        doctor.setAvatar("22965342.jpg");
      }
      _doctorRepo.save(doctor);
      return new CustomResult(200, "Create Doctor Success", null);

    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }

  }

  @Override
  public CustomResult ChangeStatus(Long id) {
    try {
      var isDoctor = _doctorRepo.findById(id);
      if (isDoctor.isPresent()) {
        Doctor doctor = isDoctor.get();
        if (doctor.getStatus().equalsIgnoreCase("active")) {
          doctor.setStatus("deactive");
        } else {
          doctor.setStatus("active");
        }
        _doctorRepo.save(doctor);
        return new CustomResult(200, "Update Success", null);
      }
      return new CustomResult(401, "Doctor does not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult Update(DoctorDto dto) {
    try {
      var isDoctor = _doctorRepo.findByCode(dto.getCode());
      if (isDoctor.size() > 0) {
        if (isDoctor.get(0).getId() != dto.getId()) {
          return new CustomResult(300, "Duplicate code", null);
        }

      }
      var isHospital = _hospitallRepo.findById(dto.getHospitalId());
      var isDepartment = _departmentRepo.findById(dto.getDepartmentId());
      var isUser = _userRepo.findById(dto.getUserId());
      if (!isHospital.isPresent()) {
        return new CustomResult(300, "Hospital is not exist", null);
      }
      if (!isDepartment.isPresent()) {
        return new CustomResult(300, "Department is not exist", null);
      }
      if (!isUser.isPresent()) {
        return new CustomResult(300, "User is not exist", null);
      }
      var isDoc = _doctorRepo.findById(dto.getId());
      if (isDoc.isPresent()) {
        Doctor doctor = isDoc.get();
        doctor.setCode(dto.getCode());
        doctor.setDepartment(isDepartment.get());
        doctor.setHospital(isHospital.get());
        doctor.setUser(isUser.get());
        doctor.setFee(dto.getFee());
        doctor.setGender(dto.getGender());
        doctor.setLevel(dto.getLevel());
        doctor.setName(dto.getName());
        doctor.setPatients(dto.getPatients());
        doctor.setRoom(dto.getRoom());
        if (dto.getStatus() != null) {
          doctor.setStatus(dto.getStatus());
        }

        doctor.setTimeWork(dto.getTimeWork());
        doctor.setWorkDay(dto.getWorkDay());
        if (dto.getAvatar() != null && !dto.getAvatar().isEmpty()) {
          doctor.setAvatar(firebaseStorageService.uploadFile(dto.getAvatar()));
        }
        _doctorRepo.save(doctor);
        return new CustomResult(200, "Update Doctor Success", null);
      }
      return new CustomResult(300, "Doctor is not exist", null);

    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }

  }

  @Override
  public CustomResult GetByHospital(Long idhospital) {
    try {
      var Listdoctor = _doctorRepo.findByHospital(idhospital);
      return new CustomResult(200, "success", Listdoctor);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult TopDoctor(DoctorTop top) {
    try {
      Date currentDate = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(currentDate);

      // Xác định ngày bắt đầu và kết thúc cho khoảng thời gian hiện tại và trước

      Date startDate, endDate;
      int time = top.getDay();

      switch (time) {
        case 1: // Daily - hôm nay và hôm qua

          setToEndOfDay(cal);
          endDate = cal.getTime();
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 7: // Weekly - tuần này và tuần trước

          setToEndOfDay(cal);
          endDate = cal.getTime();
          cal.add(Calendar.DATE, -6);
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 30: // Monthly - tháng này và tháng trước
          setToEndOfDay(cal);
          endDate = cal.getTime();
          cal.add(Calendar.DATE, -29);
          setToStartOfDay(cal);
          startDate = cal.getTime();

          break;

        case 365: // Yearly - năm nay và năm trước
          cal.set(Calendar.DAY_OF_YEAR, 1); // Đầu năm
          startDate = cal.getTime();
          cal.add(Calendar.YEAR, 1);
          endDate = cal.getTime();
          break;

        default:
          return new CustomResult(400, "Invalid time parameter", null);
      }
      Pageable pageable = PageRequest.of(0, 5);
      var rs = _bookingRepo.findTopDoctorsByBookings(top.getHospitalId(), startDate, endDate, pageable);
      return new CustomResult(200, "success", rs);
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
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 999);
  }

}
