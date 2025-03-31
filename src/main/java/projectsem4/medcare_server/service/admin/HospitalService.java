package projectsem4.medcare_server.service.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.*;
import projectsem4.medcare_server.interfaces.admin.IHospital;

import projectsem4.medcare_server.repository.admin.AttributeRepository;
import projectsem4.medcare_server.repository.admin.HospitalRepository;
import projectsem4.medcare_server.repository.admin.ServiceRepository;
import projectsem4.medcare_server.repository.admin.TypeRepository;
import projectsem4.medcare_server.repository.admin.UserRepository;
import projectsem4.medcare_server.service.FirebaseStorageService;
import org.springframework.data.domain.Page;

@Service
public class HospitalService implements IHospital {
  @Autowired
  HospitalRepository hospitalRepo;

  @Autowired
  ServiceRepository serviceRepo;

  @Autowired
  AttributeRepository AttriRepo;

  @Autowired
  FirebaseStorageService firebaseStorageService;

  @Autowired
  UserRepository userRepo;

  @Autowired
  TypeRepository typeRepo;

  @Override
  public CustomResult GetAll(FilterRes filterRes) {
    try {

      int page = filterRes.getPage() != null ? filterRes.getPage() : 0;
      int size = filterRes.getSize() != null ? filterRes.getSize() : 10;
      Page<HospitalRes> hospitalsPage = hospitalRepo.findByFilters(
          filterRes.getType(),
          filterRes.getStatus(),
          filterRes.getAddress(),
          filterRes.getSearch(),
          PageRequest.of(page, size));
      return new CustomResult(200, "Get Success", hospitalsPage);
    } catch (Exception e) {
      return new CustomResult(400, "Get Failed", e.getMessage());
    }

  }

  @Override
  public CustomResult Create(HospitalDto h) {
    try {
      var checkcode = hospitalRepo.findByCode(h.getCode());
      if (checkcode.size() > 0) {
        return new CustomResult(202, "The hospital code already exists", checkcode);
      }
      var hos = new Hospital();
      BeanUtils.copyProperties(h, hos);
      if (h.getPhotoFile() != null && !h.getPhotoFile().isEmpty()) {
        hos.setLogo(firebaseStorageService.uploadFile(h.getPhotoFile()));
      } else {
        hos.setLogo("");
      }
      StringBuilder imgBuilder = new StringBuilder();
      if (h.getPhotoImage() != null && !h.getPhotoImage().isEmpty()) {
        h.getPhotoImage().forEach(file -> {
          String filename;
          try {
            filename = firebaseStorageService.uploadFile(file);
            imgBuilder.append(filename).append("; ");
          } catch (IOException e) {
            e.printStackTrace();
          }

        });
        hos.setImage(imgBuilder.toString());
      } else {
        hos.setImage("");
      }
      hos.setStatus("deactive");
      var listService = AttriRepo.GetAttributeService();
      var type = typeRepo.findById(h.getType());
      if (type.isPresent()) {
        hos.setType(type.get());
        hospitalRepo.save(hos);
        if (!listService.isEmpty()) {
          listService.forEach(item -> {
            projectsem4.medcare_server.domain.entity.Service s = new projectsem4.medcare_server.domain.entity.Service();
            s.setHospital(hos);
            s.setAttributes(item);
            s.setStatus("deactive");
            serviceRepo.save(s);
          });
        }
        return new CustomResult(200, "Create Success", hos);
      } else {
        return new CustomResult(400, "Create fail user or type not exist", null);
      }

    } catch (Exception e) {
      return new CustomResult(400, "Create Failed", e.getMessage());
    }
  }

  @Override
  public CustomResult GetNameAndCode() {
    try {
      return new CustomResult(200, "ok", hospitalRepo.getNameAndId());
    } catch (Exception e) {
      return new CustomResult(400, "error", e.getMessage());
    }
  }

  @Override
  public CustomResult ChangeStatusHospital(Long id) {
    try {
      var hos = hospitalRepo.findById(id);
      if (hos.isPresent()) {
        Hospital p = hos.get();
        p.setStatus(p.getStatus().equalsIgnoreCase("active") ? "deactive" : "active");
        hospitalRepo.save(p);
        return new CustomResult(200, "Change Status success", p);
      } else {
        return new CustomResult(300, "Hospital not exist", null);
      }
    } catch (Exception e) {
      return new CustomResult(400, "error change status hospital", e.getMessage());
    }
  }

  @Override
  public CustomResult GetById(Long id) {
    try {
      var hos = hospitalRepo.getbyId(id);
      if (hos.size() > 0) {
        return new CustomResult(200, "get success", hos.get(0));
      } else {
        return new CustomResult(400, "Not found Hospital", null);
      }
    } catch (Exception e) {
      return new CustomResult(400, "Get Fails", e.getMessage());
    }
  }

  @Override
  public CustomResult Update(HospitalUpdateDto h) {
    try {
      var checkcode = hospitalRepo.findByCode(h.getCode());
      var hospital = hospitalRepo.findById(h.getId());
      if (hospital.isPresent()) {
        if (checkcode.size() > 0) {
          if (h.getId() != checkcode.get(0).getId()) {
            return new CustomResult(202, "The hospital code already exists", checkcode);
          }
          var istype = typeRepo.findById(h.getType());
          if (!istype.isPresent()) {
            return new CustomResult(202, "Type is not exists", null);
          }
          Hospital hos = hospital.get();

          hos.setName(h.getName());
          hos.setCode(h.getCode());
          hos.setProvince(h.getProvince());
          hos.setDistrict(h.getDistrict());
          hos.setAddress(h.getAddress());
          hos.setOpenTime(h.getOpenTime());
          hos.setCloseTime(h.getCloseTime());
          hos.setWorkDay(h.getWorkDay());
          hos.setDescription(h.getDescription());
          if (h.getPhotoFile() != null && !h.getPhotoFile().isEmpty()) {
            hos.setLogo(firebaseStorageService.uploadFile(h.getPhotoFile()));
          } else {
            hos.setLogo(h.getLogoOld());
          }
          StringBuilder imgBuilder = new StringBuilder();
          if (h.getPhotoImage() != null && !h.getPhotoImage().isEmpty()) {
            h.getPhotoImage().forEach(file -> {
              String filename;
              try {
                filename = firebaseStorageService.uploadFile(file);
                imgBuilder.append(filename).append("; ");
              } catch (IOException e) {
                e.printStackTrace();
              }

            });
            var imgString = imgBuilder.append(h.getImgOld());
            hos.setImage(imgString.toString());
          } else {
            hos.setImage(h.getImgOld());
          }
          hos.setType(istype.get());
          hospitalRepo.save(hos);
          return new CustomResult(200, "Update Success", hos);

        }
        return new CustomResult(400, "Update fail user or type not exist", null);

      }
      return new CustomResult(400, "Hospital is not exist", null);
    } catch (Exception e) {
      return new CustomResult(400, "Update Failed", e.getMessage());
    }
  }

}
