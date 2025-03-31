package projectsem4.medcare_server.interfaces.customer;

import projectsem4.medcare_server.domain.dto.CustomResult;

public interface ICustomerHospital {
    CustomResult getHospitalSuggestion();

    CustomResult GetBestPartner();

    CustomResult GetBestFacility();

    CustomResult GetDetailFacility(String id);

    CustomResult GetServiceByHospital(String id);

    CustomResult getTypeHospital();

    CustomResult GetAll(int pageNo, String value, int typeId);

    CustomResult searchAllHospital(int pageNo, String value);
}
