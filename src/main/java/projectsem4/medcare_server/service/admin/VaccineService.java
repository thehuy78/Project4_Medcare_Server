package projectsem4.medcare_server.service.admin;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Hospital;
import projectsem4.medcare_server.domain.entity.Test;
import projectsem4.medcare_server.domain.entity.User;
import projectsem4.medcare_server.domain.entity.Vaccine;
import projectsem4.medcare_server.interfaces.admin.ITesting.TestingDto;
import projectsem4.medcare_server.interfaces.admin.ITesting.TestingRes;
import projectsem4.medcare_server.interfaces.admin.IVaccine;
import projectsem4.medcare_server.repository.admin.HospitalRepository;
import projectsem4.medcare_server.repository.admin.UserRepository;
import projectsem4.medcare_server.repository.admin.VaccineRepository;

@Service
public class VaccineService implements IVaccine {
  @Autowired
  VaccineRepository _vaccineRepo;

  @Autowired
  HospitalRepository _hospitallRepo;

  @Autowired
  UserRepository _userRepo;

  @Override
  public CustomResult GetByHospital(FilterRes filterRes) {
    try {
      int page = filterRes.getPage() != null ? filterRes.getPage() : 0;
      int size = filterRes.getSize() != null ? filterRes.getSize() : 10;
      Double feeStart = null;
      Double feeEnd = null;

      if (filterRes.getFee() != null && filterRes.getFee().size() == 2) {
        feeStart = filterRes.getFee().get(0); // Kiểm tra xem có phần tử đầu tiên không
        feeEnd = filterRes.getFee().get(1); // Kiểm tra xem có phần tử thứ hai không
      } else {
        feeStart = null; // Nếu không đủ phần tử, đặt về null
        feeEnd = null; // Nếu không đủ phần tử, đặt về null
      }

      Page<VaccineRes> vaccinesPage = _vaccineRepo.findByFilters(
          filterRes.getStatus(),
          filterRes.getCodehospital(),
          filterRes.getSearch(),
          feeStart, // Truyền feeStart
          feeEnd, // Truyền feeEnd
          PageRequest.of(page, size));
      return new CustomResult(200, "success", vaccinesPage);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult GetById(Long id) {
    try {
      var isVaccine = _vaccineRepo.findById(id);
      if (isVaccine.isPresent()) {
        Vaccine vaccine = isVaccine.get();
        VaccineRes vaccineRes = new VaccineRes();
        BeanUtils.copyProperties(vaccine, vaccineRes);
        vaccineRes.setEmailUser(vaccine.getUser().getEmail());
        vaccineRes.setHospitalCode(vaccine.getHospital().getCode());
        vaccineRes.setHospitalName(vaccine.getHospital().getName());
        return new CustomResult(200, "success", vaccineRes);
      }
      return new CustomResult(300, "Vaccine is empty", null);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult Create(VaccineDto dto) {
    try {
      var isVaccine = _vaccineRepo.findByCode(dto.getCode());
      if (isVaccine.size() > 0) {
        return new CustomResult(300, "Duplicate code", null);
      }
      var isHospital = _hospitallRepo.findById(dto.getHospitalId());
      if (isHospital.isPresent()) {
        Hospital hospital = isHospital.get();
        var isUser = _userRepo.findById(dto.getUserId());
        if (isUser.isPresent()) {
          User user = isUser.get();
          Vaccine vaccine = new Vaccine();
          vaccine.setCode(dto.getCode());
          vaccine.setFee(dto.getFee());
          vaccine.setName(dto.getName());
          vaccine.setDescription(dto.getDescription());
          vaccine.setStatus("active");
          vaccine.setHospital(hospital);
          vaccine.setUser(user);
          vaccine.setFloor(dto.getFloor());
          vaccine.setZone(dto.getZone());
          _vaccineRepo.save(vaccine);
          return new CustomResult(200, "Success", null);
        }
        return new CustomResult(300, "User is empty", null);
      }
      return new CustomResult(300, "Hospital is empty", null);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult Update(VaccineDto dto) {
    try {
      var isVaccine = _vaccineRepo.findById(dto.getId());
      if (isVaccine.isPresent()) {
        var isVaccineCode = _vaccineRepo.findByCode(dto.getCode());
        if (isVaccineCode.size() > 0) {
          Vaccine iscode = isVaccineCode.get(0);
          if (iscode.getId() != dto.getId()) {
            return new CustomResult(300, "Duplicate code", null);
          }
        }
        var isHospital = _hospitallRepo.findById(dto.getHospitalId());
        if (isHospital.isPresent()) {
          Hospital hospital = isHospital.get();
          var isUser = _userRepo.findById(dto.getUserId());
          if (isUser.isPresent()) {
            User user = isUser.get();
            Vaccine vaccine = isVaccine.get();
            vaccine.setCode(dto.getCode());
            vaccine.setDescription(dto.getDescription());
            vaccine.setFee(dto.getFee());
            vaccine.setName(dto.getName());
            if (dto.getStatus() != null) {
              vaccine.setStatus(dto.getStatus());
            }

            vaccine.setUser(user);
            vaccine.setHospital(hospital);
            vaccine.setFloor(dto.getFloor());
            vaccine.setZone(dto.getZone());
            _vaccineRepo.save(vaccine);
            return new CustomResult(200, "Update Vaccine Success", null);
          }
          return new CustomResult(300, "User not exist", null);
        }

        return new CustomResult(300, "Hospital not exist", null);
      }
      return new CustomResult(300, "Vaccine not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult ChangeStatus(Long id) {
    try {
      var isVaccine = _vaccineRepo.findById(id);
      if (isVaccine.isPresent()) {
        Vaccine vaccine = isVaccine.get();
        if (vaccine.getStatus().equalsIgnoreCase("active")) {
          vaccine.setStatus("deactive");
        } else {
          vaccine.setStatus("active");
        }
        _vaccineRepo.save(vaccine);
        return new CustomResult(200, "Update Success", null);
      }
      return new CustomResult(401, "Vaccine does not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

}
