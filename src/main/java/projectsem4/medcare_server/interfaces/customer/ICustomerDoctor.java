package projectsem4.medcare_server.interfaces.customer;

import projectsem4.medcare_server.domain.dto.CustomResult;

public interface ICustomerDoctor {
    CustomResult getByDepartMentId(String id, String date);

    CustomResult getByHospitalFilter(String id, int pageNo, String searchValue,
            String filterOption);

    CustomResult getByHospitalLite(String id);

    CustomResult getDoctorBySearchAll(int pageNo, String searchValue);

    CustomResult getOfflineDay(String doctorId);
}
