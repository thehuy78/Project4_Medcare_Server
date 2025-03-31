package projectsem4.medcare_server.interfaces.customer;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;

public interface ICustomerBrief {
    CustomResult create(CustomerCreateBriefDTO customerCreateBriefDTO);

    CustomResult getByUserId(String userId);

    CustomResult delete(String id);

    CustomResult getDetail(String id);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class CustomerCreateBriefDTO {
        String user_id;
        String name;
        String phone;
        String gender;
        String dob;
        String province;
        String district;
        String ward;
        String address;
        String identifier;
        String job;

    }
}
