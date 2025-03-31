package projectsem4.medcare_server.service.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;

import projectsem4.medcare_server.domain.entity.Department;
import projectsem4.medcare_server.domain.entity.Hospital;
import projectsem4.medcare_server.domain.entity.User;
import projectsem4.medcare_server.interfaces.admin.IDepartment;

import projectsem4.medcare_server.repository.admin.DepartmentRepository;
import projectsem4.medcare_server.repository.admin.HospitalRepository;
import projectsem4.medcare_server.repository.admin.UserRepository;

@Service
public class DepartmentService implements IDepartment {
  @Autowired
  DepartmentRepository _departmentRepo;

  @Autowired
  HospitalRepository _hospitallRepo;

  @Autowired
  UserRepository _userRepo;

  @Override
  public CustomResult GetAll(FilterRes filterRes) {
    try {
      int page = filterRes.getPage() != null ? filterRes.getPage() : 0;
      int size = filterRes.getSize() != null ? filterRes.getSize() : 10;
      Page<DepartmentRes> departmentsPage = _departmentRepo.findByFilters(
          filterRes.getStatus(),
          filterRes.getCodehospital(),
          filterRes.getSearch(),
          PageRequest.of(page, size));
      return new CustomResult(200, "success", departmentsPage);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult Create(DepartmentDto dto) {
    try {
      var isDepartment = _departmentRepo.findByCode(dto.getCode());
      if (isDepartment.size() > 0) {
        return new CustomResult(400, "Duplicate code", null);
      }
      var isHospital = _hospitallRepo.findById(dto.getHospitalId());
      if (isHospital.isPresent()) {
        Hospital hospital = isHospital.get();
        var isUser = _userRepo.findById(dto.getUserId());
        if (isUser.isPresent()) {
          User user = isUser.get();
          Department department = new Department();
          department.setName(dto.getName());
          department.setCode(dto.getCode());
          department.setFloor(dto.getFloor());
          department.setZone(dto.getZone());
          department.setStatus("active");
          department.setHospital(hospital);
          department.setUser(user);
          _departmentRepo.save(department);
          return new CustomResult(200, "Create Success", null);
        }
        return new CustomResult(401, "Account does not exist", null);
      }

      return new CustomResult(400, "Hospital does not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult Update(DepartmentDto dto) {
    try {
      var isDepartment = _departmentRepo.findById(dto.getId());
      if (!isDepartment.isPresent()) {
        return new CustomResult(400, "Department is not exist", null);
      }
      var isDepartmentCode = _departmentRepo.findByCode(dto.getCode());
      if (isDepartmentCode.size() > 0) {
        Department de = isDepartmentCode.get(0);
        if (de.getId() != dto.getId()) {
          return new CustomResult(400, "Duplicate code", null);
        }
      }
      Department department = isDepartment.get();
      var isUser = _userRepo.findById(dto.getUserId());
      if (isUser.isPresent()) {
        User user = isUser.get();
        department.setName(dto.getName());
        department.setCode(dto.getCode());
        department.setFloor(dto.getFloor());
        department.setZone(dto.getZone());
        if (dto.getStatus() != null) {
          department.setStatus(dto.getStatus());
        }
        department.setUser(user);
        _departmentRepo.save(department);
        return new CustomResult(200, "Update Success", null);
      }
      return new CustomResult(401, "Account does not exist", null);

    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult ChangeStatus(Long id) {
    try {
      var isDepartment = _departmentRepo.findById(id);
      if (isDepartment.isPresent()) {
        Department department = isDepartment.get();
        if (department.getStatus().equalsIgnoreCase("active")) {
          department.setStatus("deactive");
        } else {
          department.setStatus("active");
        }
        _departmentRepo.save(department);
        return new CustomResult(200, "Update Success", null);
      }
      return new CustomResult(401, "Department does not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

}
