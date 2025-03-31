package projectsem4.medcare_server.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Notification;
import projectsem4.medcare_server.interfaces.admin.IHospital.HospitalRes;
import projectsem4.medcare_server.interfaces.admin.INotification;
import projectsem4.medcare_server.repository.admin.NotificationRepository;
import projectsem4.medcare_server.repository.admin.UserRepository;

@Service
public class NotificationService implements INotification {
  @Autowired
  NotificationRepository _NotiRepo;

  @Autowired
  UserRepository _UserRepo;

  @Override
  public CustomResult GetAll(filterRes res) {
    try {
      int page = res.getPage() != null ? res.getPage() : 0;
      int size = res.getSize() != null ? res.getSize() : 10;
      Page<NotificationRes> notificationPage = _NotiRepo.findByFilters(
          res.getIdUser(),
          res.getType(),
          res.getStartDate(),
          res.getEndDate(),
          PageRequest.of(page, size));
      return new CustomResult(200, "Get Success", notificationPage);
    } catch (Exception e) {
      return new CustomResult(400, "Get Failed", null);
    }

  }

  @Override
  public CustomResult CreateByAdmin(createNoti create) {
    try {
      var u = _UserRepo.GetUserByRole(create.getRole());
      if (u.size() > 0) {
        u.forEach(user -> {
          Notification noti = new Notification();
          noti.setIsDeleted("undeleted");
          noti.setStatus("unread");
          noti.setUser(user);
          noti.setDescription(create.getDescription());
          noti.setType(create.getRole() + ":setting");
          _NotiRepo.save(noti);
        });
      }
      return new CustomResult(200, "Get success", u);
    } catch (Exception e) {
      return new CustomResult(400, "Get Failed", e.getMessage());
    }
  }

  @Override
  public CustomResult DeletedById(Long id) {
    try {

      var isNoti = _NotiRepo.findById(id);
      if (isNoti.isPresent()) {
        Notification noti = isNoti.get();
        noti.setIsDeleted("deleted ");
        _NotiRepo.save(noti);
        return new CustomResult(200, "Deleted success", null);
      }
      return new CustomResult(400, "Not found notification", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult DeletedMulti(List<Long> list) {
    try {
      list.forEach(idNoti -> {
        var isNoti = _NotiRepo.findById(idNoti);
        if (isNoti.isPresent()) {
          Notification noti = isNoti.get();
          noti.setIsDeleted("deleted");
          _NotiRepo.save(noti);
        }
      });
      return new CustomResult(200, "Deleted success " + list.size() + " item", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Transactional
  @Override
  public CustomResult DeletedAll(Long id) {
    try {
      _NotiRepo.updateIsDeletedByUserId(id);
      return new CustomResult(200, "Deleted all success", null);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

}
