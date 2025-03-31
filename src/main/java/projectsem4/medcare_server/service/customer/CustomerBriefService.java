package projectsem4.medcare_server.service.customer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Brief;
import projectsem4.medcare_server.domain.entity.User;
import projectsem4.medcare_server.interfaces.customer.ICustomerBrief;
import projectsem4.medcare_server.repository.customer.CustomerAuthRepo;
import projectsem4.medcare_server.repository.customer.CustomerBriefRepo;

@Service
public class CustomerBriefService implements ICustomerBrief {
    @Autowired
    CustomerBriefRepo _CustomerBriefRepo;

    @Autowired
    CustomerAuthRepo _CustomerAuthRepo;

    @Override
    public CustomResult create(CustomerCreateBriefDTO customerCreateBriefDTO) {
        try {
            User u = _CustomerAuthRepo.findById(Long.parseLong(customerCreateBriefDTO.getUser_id())).get();
            if (u == null) {
                return new CustomResult(201, "User not found", null);
            }
            List<Brief> listbr = _CustomerBriefRepo.findByUser(u);
            if (listbr.size() >= 10) {
                return new CustomResult(202, "Limit Create", null);
            }
            Brief br = new Brief();
            br.setGender(customerCreateBriefDTO.getGender().equals("0") ? "Male" : "Female");
            br.setAddress(customerCreateBriefDTO.getAddress());
            br.setDistrict(customerCreateBriefDTO.getDistrict());
            br.setIdentifier(
                    customerCreateBriefDTO.getIdentifier() != null ? customerCreateBriefDTO.getIdentifier() : null);
            br.setJob(customerCreateBriefDTO.getJob() != null ? customerCreateBriefDTO.getJob() : null);
            br.setUser(u);
            br.setName(customerCreateBriefDTO.getName());
            br.setPhone(customerCreateBriefDTO.getPhone());
            br.setProvince(customerCreateBriefDTO.getProvince());
            br.setWard(customerCreateBriefDTO.getWard());
            br.setStatus("active");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            LocalDateTime localDate = LocalDateTime.parse(customerCreateBriefDTO.getDob(), formatter);
            Date date = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
            br.setDob(date);
            _CustomerBriefRepo.save(br);
            return new CustomResult(200, "success", null);

        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult getByUserId(String userId) {
        try {
            User u = _CustomerAuthRepo.findById(Long.parseLong(userId)).get();
            if (u == null) {
                return new CustomResult(201, "User not found", null);
            }
            return new CustomResult(200, "Success", _CustomerBriefRepo.findByUserId(u));
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult delete(String id) {
        try {
            Brief br = _CustomerBriefRepo.findById(Long.parseLong(id)).get();
            br.setStatus("deactive");
            _CustomerBriefRepo.save(br);

            return new CustomResult(200, "Success", null);
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult getDetail(String id) {
        try {
            return new CustomResult(200, "Success", _CustomerBriefRepo.findByIdCustom(Long.parseLong(id)));
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

}
