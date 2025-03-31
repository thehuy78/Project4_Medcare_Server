package projectsem4.medcare_server.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.ITransaction;
import projectsem4.medcare_server.interfaces.admin.ITransaction.Filter;;

@RestController
@RequestMapping("/api/admin/transaction")
public class TransactionController {
  @Autowired
  ITransaction _Itrans;

  @PostMapping("getall")
  @RolesAllowed({ "samedcare" })
  public CustomResult GetAll(@RequestBody Filter filter) {
    return _Itrans.GetAll(filter);
  }

  @PostMapping("export")
  @RolesAllowed({ "samedcare" })
  public CustomResult Export(@RequestBody Filter filter) {
    return _Itrans.ExportFile(filter);
  }

}
