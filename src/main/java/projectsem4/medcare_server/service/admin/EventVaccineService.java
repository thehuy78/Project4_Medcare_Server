package projectsem4.medcare_server.service.admin;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.EventVaccine;

import projectsem4.medcare_server.domain.entity.User;
import projectsem4.medcare_server.interfaces.admin.IEventVaccine;

import projectsem4.medcare_server.repository.admin.EventVaccineRepository;
import projectsem4.medcare_server.repository.admin.UserRepository;

@Service
public class EventVaccineService implements IEventVaccine {

  @Autowired
  EventVaccineRepository _EventVaccineRepo;
  @Autowired
  UserRepository _UserRepo;

  @Override
  public CustomResult GetAll(Filter res) {
    try {
      int page = res.getPage() != null ? res.getPage() : 0;
      int size = res.getSize() != null ? res.getSize() : 10;
      Page<EventVaccineRes> eventVaccine = _EventVaccineRepo.findAllByFillter(
          res.getStatus(),
          res.getSearch(),
          PageRequest.of(page, size));
      return new CustomResult(200, "Get Success", eventVaccine);
    } catch (Exception e) {
      return new CustomResult(400, "Get Failed", e.getMessage());
    }
  }

  @Override
  public CustomResult Create(EventVaccineDto dto) {
    try {
      var isEvent = _EventVaccineRepo.findByCode(dto.getCode());
      if (isEvent.size() > 0) {
        return new CustomResult(300, "Duplicate code", null);
      }
      var isUser = _UserRepo.findById(dto.getUserId());
      if (isUser.isPresent()) {
        User user = isUser.get();
        EventVaccine event = new EventVaccine();
        event.setCode(dto.getCode());
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setStatus("active");
        event.setUser(user);
        _EventVaccineRepo.save(event);
        return new CustomResult(200, "Success", null);
      }
      return new CustomResult(300, "User is empty", null);
    } catch (

    Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult Update(EventVaccineDto dto) {
    try {
      var isEvent = _EventVaccineRepo.findById(dto.getId());
      if (isEvent.isPresent()) {
        var isEventCode = _EventVaccineRepo.findByCode(dto.getCode());
        if (isEventCode.size() > 0) {
          EventVaccine iscode = isEventCode.get(0);
          if (iscode.getId() != dto.getId()) {
            return new CustomResult(300, "Duplicate code", null);
          }
        }
        var isUser = _UserRepo.findById(dto.getUserId());
        if (isUser.isPresent()) {
          User user = isUser.get();
          EventVaccine event = isEvent.get();
          event.setCode(dto.getCode());
          event.setName(dto.getName());
          event.setDescription(dto.getDescription());
          event.setUser(user);
          _EventVaccineRepo.save(event);
          return new CustomResult(200, "Success", null);
        }
        return new CustomResult(300, "User is empty", null);
      }
      return new CustomResult(300, "Event Vaccine not exist", null);
    } catch (

    Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult ChangeStatus(Long id) {
    try {
      var isEvent = _EventVaccineRepo.findById(id);
      if (isEvent.isPresent()) {
        EventVaccine event = isEvent.get();
        if (event.getStatus().equalsIgnoreCase("active")) {
          event.setStatus("deactive");
        } else {
          event.setStatus("active");
        }
        _EventVaccineRepo.save(event);
        return new CustomResult(200, "Update Success", null);
      }
      return new CustomResult(401, "Event Vaccine does not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult GetById(Long id) {
    try {
      var rs = _EventVaccineRepo.findById(id);
      if (rs.isPresent()) {
        EventVaccineRes event = new EventVaccineRes();
        BeanUtils.copyProperties(rs.get(), event);
        event.setEmailUser(rs.get().getUser().getEmail());
        return new CustomResult(200, "Get Success", event);
      }
      return new CustomResult(401, "Event Vaccine does not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

}
