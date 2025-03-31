package projectsem4.medcare_server.interfaces.customer;

import projectsem4.medcare_server.domain.dto.CustomResult;

public interface ICustomerDepartment {
    CustomResult GetAll();

    CustomResult getByHospitalId(String id);

    CustomResult getDepartmentBySearchAll(int pageNo, String searchValue);
}
