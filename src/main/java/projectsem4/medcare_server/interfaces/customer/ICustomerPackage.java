package projectsem4.medcare_server.interfaces.customer;

import projectsem4.medcare_server.domain.dto.CustomResult;

public interface ICustomerPackage {

    CustomResult GetBestPackageHealthCare();

    CustomResult GetBestPackageLabTest();

    CustomResult GetBestPackageVaccination();

    CustomResult GetDetailPackage(String attributeId, String id);

    CustomResult GetPackageByHospital(String id, int pageNo, int attributeId, String searchValue,
            String filterHospitalName);

    CustomResult getPackageSuggestion(String id, int attributeId);

    CustomResult searchAllPack(int pageNo, String searchValue);

}
