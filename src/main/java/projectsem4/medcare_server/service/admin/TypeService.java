package projectsem4.medcare_server.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.IType;
import projectsem4.medcare_server.repository.admin.TypeRepository;

@Service
public class TypeService implements IType {

  @Autowired
  TypeRepository _typeRepo;

  @Override
  public CustomResult GetTypeBooking() {
    try {
      var rs = _typeRepo.GetTypeBooking();
      return new CustomResult(200, "Get success", rs);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult GetTypeHospital() {
    try {
      var rs = _typeRepo.GetTypeHospital();
      return new CustomResult(200, "Get success", rs);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }
}
