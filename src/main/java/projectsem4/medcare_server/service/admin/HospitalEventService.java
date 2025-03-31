package projectsem4.medcare_server.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.HospitalEvent;
import projectsem4.medcare_server.interfaces.admin.IHospitalEvent;
import projectsem4.medcare_server.repository.admin.EventVaccineRepository;
import projectsem4.medcare_server.repository.admin.HospitalEventRepository;
import projectsem4.medcare_server.repository.admin.UserRepository;
import projectsem4.medcare_server.repository.admin.VaccineRepository;

@Service
public class HospitalEventService implements IHospitalEvent {
  @Autowired
  HospitalEventRepository _HospitalEventRepo;
  @Autowired
  UserRepository _UserRepo;
  @Autowired
  VaccineRepository _vaccineRepo;
  @Autowired
  EventVaccineRepository _eventRepo;

  @Override
  public CustomResult GetByEvent(Filter filter) {
    try {
      List<HospitalEventRes> rs = _HospitalEventRepo.hospitalInEvent(filter.getHospitalId(), filter.getIdEvent());
      return new CustomResult(200, "Get Success", rs);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult AddHospitalEvent(AddDto dto) {
    try {
      var isUser = _UserRepo.findById(dto.getIdUser());
      if (!isUser.isPresent()) {
        return new CustomResult(400, "User is not exist", null);
      }
      var isEvent = _eventRepo.findById(dto.getIdEvent());
      if (!isEvent.isPresent()) {
        return new CustomResult(400, "Event Vaccine is not exist", null);
      }
      var isVaccine = _vaccineRepo.findById(dto.getIdVaccine());
      if (!isVaccine.isPresent()) {
        return new CustomResult(400, "Vaccine is not exist", null);
      }
      HospitalEvent hosEvent = new HospitalEvent();
      hosEvent.setStatus("deactive");
      hosEvent.setUser(isUser.get());
      hosEvent.setEventVaccine(isEvent.get());
      hosEvent.setVaccine(isVaccine.get());
      _HospitalEventRepo.save(hosEvent);
      return new CustomResult(200, "Send Request Success", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult RemoveHospitalEvent(Long id) {
    try {
      _HospitalEventRepo.deleteById(id);
      return new CustomResult(200, "Remove Success", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult ChangeStatus(Long id) {
    try {
      var rs = _HospitalEventRepo.findById(id);
      if (rs.isPresent()) {
        HospitalEvent hos = rs.get();
        if (hos.getStatus().equalsIgnoreCase("deactive")) {
          hos.setStatus("active");
          _HospitalEventRepo.save(hos);
          return new CustomResult(200, "Accept Success", null);
        } else {
          return new CustomResult(200, "Accept Success", null);
        }
      }
      return new CustomResult(400, "Vaccine event is not Exist", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult Denied(Long id) {
    try {
      _HospitalEventRepo.deleteById(id);
      return new CustomResult(200, "Remove Success", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

}
