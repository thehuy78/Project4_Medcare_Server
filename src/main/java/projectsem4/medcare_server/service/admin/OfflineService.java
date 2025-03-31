package projectsem4.medcare_server.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Doctor;
import projectsem4.medcare_server.domain.entity.Offline;
import projectsem4.medcare_server.interfaces.admin.IHospital.HospitalRes;
import projectsem4.medcare_server.interfaces.admin.IOffline;
import projectsem4.medcare_server.repository.admin.DoctorRepository;
import projectsem4.medcare_server.repository.admin.OfflineRepository;
import projectsem4.medcare_server.repository.admin.UserRepository;
import org.springframework.data.domain.Page;

@Service
public class OfflineService implements IOffline {

  @Autowired
  OfflineRepository _offlineRepo;
  @Autowired
  DoctorRepository _doctorRepo;
  @Autowired
  UserRepository _userRepo;

  @Override
  public CustomResult Create(OfflineDto dto) {
    try {
      var isUser = _userRepo.findById(dto.getUserId());
      if (!isUser.isPresent()) {
        return new CustomResult(300, "User is not Exist", null);
      }
      if (dto.getIdDoctor().size() > 0) {
        dto.getIdDoctor().forEach(item -> {
          var isDoctor = _doctorRepo.findById(item);
          if (isDoctor.isPresent()) {
            Offline off = new Offline();
            off.setDoctor(isDoctor.get());
            off.setUser(isUser.get());
            off.setDay(dto.getDay());
            off.setDescription(dto.getDescription());
            off.setStatus("active");
            off.setHospital(isDoctor.get().getHospital());
            _offlineRepo.save(off);
          }

        });
        return new CustomResult(200, "Success", null);
      }
      return new CustomResult(400, "Doctor ID not Null", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult GetByHospital(OfflineFilter filter) {
    try {
      int page = filter.getPage() != null ? filter.getPage() : 0;
      int size = filter.getSize() != null ? filter.getSize() : 10;
      Page<OfflineRes> data = _offlineRepo.findByFilters(
          filter.getStatus(),
          filter.getSearch(),
          filter.getIdHospital(),
          PageRequest.of(page, size));
      return new CustomResult(200, "Success", data);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

}
