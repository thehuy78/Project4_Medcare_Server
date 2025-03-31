package projectsem4.medcare_server.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Hospital;
import projectsem4.medcare_server.interfaces.customer.ICustomerDepartment;
import projectsem4.medcare_server.interfaces.customer.ICustomerHospital;
import projectsem4.medcare_server.repository.customer.CustomerDepartmentRepo;
import projectsem4.medcare_server.repository.customer.CustomerHospitalRepo;

@Service
public class CustomerDepartmentService implements ICustomerDepartment {
    @Autowired
    CustomerDepartmentRepo _CustomerDepartmentRepo;

    @Autowired
    CustomerHospitalRepo _CustomerHospitalRepo;

    @Override
    public CustomResult getByHospitalId(String id) {
        try {
            Hospital h = _CustomerHospitalRepo.findById(Long.parseLong(id)).get();
            return new CustomResult(200, "success", _CustomerDepartmentRepo.findByHospital(h));
        } catch (Exception e) {
            return new CustomResult(400, " Server Error", null);
        }
    }

    @Override
    public CustomResult GetAll() {
        try {
            return new CustomResult(200, "", _CustomerDepartmentRepo.GetAllCustom());
        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResult getDepartmentBySearchAll(int pageNo, String searchValue) {
        try {
            Pageable pageable = PageRequest.of(pageNo, 4);
            return new CustomResult(200, "Success",
                    _CustomerDepartmentRepo.DepartmentWithHospitalName(searchValue, pageable));

        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

}
