package projectsem4.medcare_server.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.INotification;
import projectsem4.medcare_server.interfaces.admin.INotification.createNoti;
import projectsem4.medcare_server.interfaces.admin.INotification.filterRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;;

@RestController
@RequestMapping("/api/admin/notification")
public class NotificationController {
  @Autowired
  INotification _INotification;

  @PostMapping("getall")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult getall(@RequestBody filterRes res) {
    return _INotification.GetAll(res);
  }

  @PostMapping("create")
  @RolesAllowed({ "samedcare" })
  public CustomResult CreateNoti(@RequestBody createNoti res) {
    return _INotification.CreateByAdmin(res);
  }

  @GetMapping("deleted/{id}")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult DeletedById(@PathVariable Long id) {
    return _INotification.DeletedById(id);
  }

  @PostMapping("deletedMulti")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult DeletedMulti(@RequestBody Map<String, List<Long>> request) {
    List<Long> listId = request.get("checked");
    return _INotification.DeletedMulti(listId);
  }

  @GetMapping("deletedAll/{id}")
  @RolesAllowed({ "samedcare", "admin" })
  public CustomResult DeleteAll(@PathVariable Long id) {
    return _INotification.DeletedAll(id);
  }

}
