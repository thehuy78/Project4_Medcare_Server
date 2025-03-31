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
import projectsem4.medcare_server.domain.entity.User;
import projectsem4.medcare_server.interfaces.admin.IDepartment.DepartmentRes;
import projectsem4.medcare_server.interfaces.admin.IPack;
import projectsem4.medcare_server.repository.admin.HospitalRepository;
import projectsem4.medcare_server.repository.admin.PackRepository;
import projectsem4.medcare_server.repository.admin.UserRepository;

@Service
public class PackService implements IPack {

  @Autowired
  PackRepository _packRepo;

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

      Page<PackRes> packsPage = _packRepo.findByFilters(
          filterRes.getStatus(),
          filterRes.getCodehospital(),
          filterRes.getSearch(),
          feeStart, // Truyền feeStart
          feeEnd, // Truyền feeEnd
          PageRequest.of(page, size));
      return new CustomResult(200, "success", packsPage);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult GetById(Long id) {
    try {
      var ispacks = _packRepo.findById(id);
      if (ispacks.isPresent()) {
        Pack pack = ispacks.get();
        PackRes packres = new PackRes();
        BeanUtils.copyProperties(pack, packres);
        packres.setHospitalCode(pack.getHospital().getCode());
        packres.setHospitalName(pack.getHospital().getName());
        packres.setEmailUser(pack.getUser().getEmail());
        return new CustomResult(200, "success", packres);
      }
      return new CustomResult(300, "Package is empty", null);

    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult Create(PackageDto dto) {
    try {
      var ispacks = _packRepo.findByCode(dto.getCode());
      if (ispacks.size() > 0) {
        return new CustomResult(300, "Duplicate code", null);
      }
      var isHospital = _hospitallRepo.findById(dto.getHospitalId());
      if (isHospital.isPresent()) {
        Hospital hospital = isHospital.get();
        var isUser = _userRepo.findById(dto.getUserId());
        if (isUser.isPresent()) {
          User user = isUser.get();
          Pack pack = new Pack();
          pack.setCode(dto.getCode());
          pack.setFee(dto.getFee());
          pack.setName(dto.getName());
          pack.setDescription(dto.getDescription());
          pack.setStatus("active");
          pack.setHospital(hospital);
          pack.setUser(user);
          _packRepo.save(pack);
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
  public CustomResult Update(PackageDto packageDto) {
    try {
      var isPackage = _packRepo.findById(packageDto.getId());
      if (isPackage.isPresent()) {
        var isPackageCode = _packRepo.findByCode(packageDto.getCode());
        if (isPackageCode.size() > 0) {
          Pack iscode = isPackageCode.get(0);
          if (iscode.getId() != packageDto.getId()) {
            return new CustomResult(300, "Duplicate code", null);
          }
        }
        var isHospital = _hospitallRepo.findById(packageDto.getHospitalId());
        if (isHospital.isPresent()) {
          Hospital hospital = isHospital.get();
          var isUser = _userRepo.findById(packageDto.getUserId());
          if (isUser.isPresent()) {
            User user = isUser.get();
            Pack pack = isPackage.get();
            pack.setCode(packageDto.getCode());
            pack.setDescription(packageDto.getDescription());
            pack.setFee(packageDto.getFee());
            pack.setName(packageDto.getName());
            if (packageDto.getStatus() != null) {
              pack.setStatus(packageDto.getStatus());
            }

            pack.setUser(user);
            pack.setHospital(hospital);
            _packRepo.save(pack);
            return new CustomResult(200, "Update Package Success", null);
          }
          return new CustomResult(300, "User not exist", null);
        }

        return new CustomResult(300, "Hospital not exist", null);
      }
      return new CustomResult(300, "Package not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult ChangeStatus(Long id) {
    try {
      var isPackage = _packRepo.findById(id);
      if (isPackage.isPresent()) {
        Pack pack = isPackage.get();
        if (pack.getStatus().equalsIgnoreCase("active")) {
          pack.setStatus("deactive");
        } else {
          pack.setStatus("active");
        }
        _packRepo.save(pack);
        return new CustomResult(200, "Update Success", null);
      }
      return new CustomResult(401, "Package does not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

}
