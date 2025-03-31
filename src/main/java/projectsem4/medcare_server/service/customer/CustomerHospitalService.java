package projectsem4.medcare_server.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.customer.ICustomerHospital;
import projectsem4.medcare_server.repository.customer.CustomerBookingRepo;
import projectsem4.medcare_server.repository.customer.CustomerDepartmentRepo;
import projectsem4.medcare_server.repository.customer.CustomerHospitalRepo;
import projectsem4.medcare_server.repository.customer.CustomerHospitalServiceRepo;
import projectsem4.medcare_server.repository.customer.CustomerTypeRepo;

@Service
public class CustomerHospitalService implements ICustomerHospital {

    @Autowired
    private CustomerHospitalRepo _CustomerHospitalRepo;

    @Autowired
    private CustomerHospitalServiceRepo _CustomerHospitalServiceRepo;

    @Autowired
    private CustomerTypeRepo _CustomerTypeRepo;

    @Autowired
    private CustomerBookingRepo _CustomerBookingRepo;

    @Autowired
    private CustomerDepartmentRepo _CustomerDepartmentRepo;

    @Override
    public CustomResult getHospitalSuggestion() {
        try {
            return new CustomResult(200, "", _CustomerHospitalRepo.getHospitalSuggestion());
        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }

    }

    @Override
    public CustomResult GetBestPartner() {

        try {
            return new CustomResult(200, "", _CustomerHospitalRepo.GetBestPartner());
        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResult GetBestFacility() {
        try {
            return new CustomResult(200, "", _CustomerBookingRepo.GetBestFacility());
        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResult GetDetailFacility(String id) {
        try {
            return new CustomResult(200, "", _CustomerHospitalRepo.GetDetailFacility(Long.parseLong(id)));
        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResult GetServiceByHospital(String id) {
        try {
            return new CustomResult(200, "", _CustomerHospitalServiceRepo.GetService(Long.parseLong(id)));
        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResult GetAll(int PageNo, String searchValue, int typeId) {
        try {
            Pageable pageable = PageRequest.of(PageNo, 4);
            if (typeId == 0) {
                return new CustomResult(200, "", _CustomerHospitalRepo.getAllCustom(pageable, searchValue, null));
            }
            return new CustomResult(200, "",
                    _CustomerHospitalRepo.getAllCustom(pageable, searchValue, Long.valueOf(typeId)));

        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResult getTypeHospital() {
        try {

            return new CustomResult(200, "", _CustomerTypeRepo.GetTypeCustom());
        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResult searchAllHospital(int pageNo, String value) {
        try {
            Pageable pageable = PageRequest.of(pageNo, 4);

            return new CustomResult(200, "",
                    _CustomerDepartmentRepo.searchAllHospital(pageable, value));

        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

}
