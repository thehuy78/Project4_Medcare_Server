package projectsem4.medcare_server.service.admin;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Department;
import projectsem4.medcare_server.domain.entity.Hospital;
import projectsem4.medcare_server.domain.entity.Pack;
import projectsem4.medcare_server.domain.entity.Test;
import projectsem4.medcare_server.domain.entity.User;
import projectsem4.medcare_server.interfaces.admin.IPack.PackRes;
import projectsem4.medcare_server.interfaces.admin.IPack.PackageDto;
import projectsem4.medcare_server.repository.admin.HospitalRepository;
import projectsem4.medcare_server.repository.admin.TestingRepositoty;
import projectsem4.medcare_server.repository.admin.UserRepository;
import projectsem4.medcare_server.interfaces.admin.ITesting;

@Service
public class TestingService implements ITesting {

  @Autowired
  TestingRepositoty _testingRepo;

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
      Page<TestingRes> testingsPage = _testingRepo.findByFilters(
          filterRes.getStatus(),
          filterRes.getCodehospital(),
          filterRes.getSearch(),
          feeStart, // Truyền feeStart
          feeEnd, // Truyền feeEnd
          PageRequest.of(page, size));
      return new CustomResult(200, "success", testingsPage);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult GetById(Long id) {
    try {
      var isTesting = _testingRepo.findById(id);
      if (isTesting.isPresent()) {
        projectsem4.medcare_server.domain.entity.Test test = isTesting.get();
        TestingRes testingRes = new TestingRes();
        BeanUtils.copyProperties(test, testingRes);
        testingRes.setEmailUser(test.getUser().getEmail());
        testingRes.setHospitalName(test.getHospital().getName());
        testingRes.setHospitalCode(test.getHospital().getCode());
        return new CustomResult(200, "Success", testingRes);
      }
      return new CustomResult(300, "Testing is empty", null);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult Create(TestingDto dto) {
    try {
      var isTesting = _testingRepo.findByCode(dto.getCode());
      if (isTesting.size() > 0) {
        return new CustomResult(300, "Duplicate code", null);
      }
      var isHospital = _hospitallRepo.findById(dto.getHospitalId());
      if (isHospital.isPresent()) {
        Hospital hospital = isHospital.get();
        var isUser = _userRepo.findById(dto.getUserId());
        if (isUser.isPresent()) {
          User user = isUser.get();
          projectsem4.medcare_server.domain.entity.Test test = new projectsem4.medcare_server.domain.entity.Test();
          test.setCode(dto.getCode());
          test.setFee(dto.getFee());
          test.setName(dto.getName());
          test.setDescription(dto.getDescription());
          test.setStatus("active");
          test.setHospital(hospital);
          test.setUser(user);
          test.setFloor(dto.getFloor());
          test.setZone(dto.getZone());
          _testingRepo.save(test);
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
  public CustomResult Update(TestingDto dto) {
    try {
      var isTesting = _testingRepo.findById(dto.getId());
      if (isTesting.isPresent()) {
        var isTestingCode = _testingRepo.findByCode(dto.getCode());
        if (isTestingCode.size() > 0) {
          Test iscode = isTestingCode.get(0);
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
            Test test = isTesting.get();
            test.setCode(dto.getCode());
            test.setDescription(dto.getDescription());
            test.setFee(dto.getFee());
            test.setName(dto.getName());
            if (dto.getStatus() != null) {
              test.setStatus(dto.getStatus());
            }

            test.setUser(user);
            test.setHospital(hospital);
            test.setFloor(dto.getFloor());
            test.setZone(dto.getZone());
            _testingRepo.save(test);
            return new CustomResult(200, "Update Testing Success", null);
          }
          return new CustomResult(300, "User not exist", null);
        }

        return new CustomResult(300, "Hospital not exist", null);
      }
      return new CustomResult(300, "Testing not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult ChangeStatus(Long id) {
    try {
      var isTesting = _testingRepo.findById(id);
      if (isTesting.isPresent()) {
        Test testing = isTesting.get();
        if (testing.getStatus().equalsIgnoreCase("active")) {
          testing.setStatus("deactive");
        } else {
          testing.setStatus("active");
        }
        _testingRepo.save(testing);
        return new CustomResult(200, "Update Success", null);
      }
      return new CustomResult(401, "Testing does not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

}
