package projectsem4.medcare_server.service.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Attributes;
import projectsem4.medcare_server.interfaces.admin.IHospital;
import projectsem4.medcare_server.interfaces.admin.IHospital.HospitalResNameAndCodeAndId;
import projectsem4.medcare_server.interfaces.admin.IServices;
import projectsem4.medcare_server.interfaces.admin.IServices.HospitalWithServices;
import projectsem4.medcare_server.interfaces.admin.IServices.ServicesRes;
import projectsem4.medcare_server.repository.admin.AttributeRepository;
import projectsem4.medcare_server.repository.admin.HospitalRepository;
import projectsem4.medcare_server.repository.admin.ServiceRepository;

@Service
public class ServiceHospitalService implements IServices {

  @Autowired
  IHospital _IHospital;
  @Autowired
  HospitalRepository HospitalRepo;

  @Autowired
  AttributeRepository attributeRepo;
  @Autowired
  ServiceRepository ServiceRepo;

  @Override
  public CustomResult GetHospitalService(Integer page, Integer size, String search) {
    try {
      // Use PageRequest for pagination
      Page<HospitalResNameAndCodeAndId> hospitalsPage = HospitalRepo.getNameAndCodeAndId(
          search,
          PageRequest.of(page, size));

      // Fetch services
      var services = ServiceRepo.GetAll(); // List of ServicesRes

      // Map hospitals to their services
      List<HospitalWithServices> hospitalsWithServices = hospitalsPage.getContent().stream()
          .map(hospital -> {
            List<ServicesRes> filteredServices = services.stream()
                .filter(service -> service.getIdHospital().equals(hospital.getId()))
                .collect(Collectors.toList());

            return new HospitalWithServices(
                hospital.getId(),
                hospital.getCode(),
                hospital.getName(),
                filteredServices);
          })
          .collect(Collectors.toList());

      // Create a response object with pagination metadata
      PaginatedCustomResult result = new PaginatedCustomResult(
          hospitalsPage.getTotalPages(),
          hospitalsPage.getTotalElements(),
          hospitalsPage.getNumber(),
          hospitalsWithServices);

      return new CustomResult(200, "get success", result);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class PaginatedCustomResult extends CustomResult {
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private Object data;
  }

  @Override
  public CustomResult ChangeStatusService(Long id) {
    try {
      var service = ServiceRepo.findById(id);
      if (service.isPresent()) {
        projectsem4.medcare_server.domain.entity.Service s = service.get();
        if (s.getStatus().equalsIgnoreCase("active")) {
          s.setStatus("deactive");
        } else {
          s.setStatus("active");
        }
        ServiceRepo.save(s);
      }
      return new CustomResult(200, "get success", null);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult GetService() {
    try {
      return new CustomResult(200, "get success", attributeRepo.GetAttributeService());
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult GetServiceByHospital(String hospitalCode) {
    try {
      return new CustomResult(200, "get success", ServiceRepo.GetByHospital(
          hospitalCode));
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult AddService(String name) {
    try {
      Attributes a = new Attributes();
      a.setName(name);
      a.setType("service");
      a.setStatus("active");
      attributeRepo.save(a);
      var listhos = HospitalRepo.findAll();
      if (listhos.size() > 0) {
        listhos.forEach(item -> {
          projectsem4.medcare_server.domain.entity.Service s = new projectsem4.medcare_server.domain.entity.Service();
          s.setHospital(item);
          s.setAttributes(a);
          s.setStatus("deactive");
          ServiceRepo.save(s);
        });
      }
      return new CustomResult(200, "add success", a);

    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }

  }

  @Override
  public CustomResult EditService(AttributeRes res) {
    try {
      var attribute = attributeRepo.findById(res.getId());
      if (attribute.isPresent()) {
        Attributes a = attribute.get();
        a.setName(res.getName());
        attributeRepo.save(a);
        return new CustomResult(200, "add success", a);
      }
      return new CustomResult(202, "service not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

}