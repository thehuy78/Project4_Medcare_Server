package projectsem4.medcare_server.service.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Hospital;
import projectsem4.medcare_server.domain.entity.Pack;
import projectsem4.medcare_server.domain.entity.Test;
import projectsem4.medcare_server.domain.entity.Vaccine;
import projectsem4.medcare_server.interfaces.customer.ICustomerPackage;
import projectsem4.medcare_server.repository.customer.CustomerHospitalRepo;
import projectsem4.medcare_server.repository.customer.CustomerLabTestRepo;
import projectsem4.medcare_server.repository.customer.CustomerPackageRepo;
import projectsem4.medcare_server.repository.customer.CustomerVaccinationRepo;
import org.json.JSONObject;
import org.json.JSONArray;

@Service
public class CustomerPackageService implements ICustomerPackage {

    @Autowired
    CustomerPackageRepo _CustomerPackageRepo;

    @Autowired
    CustomerLabTestRepo _CustomerLabTestRepo;

    @Autowired
    CustomerVaccinationRepo _CustomerVaccinationRepo;

    @Autowired
    CustomerHospitalRepo _CustomerHospitalRepo;

    @Override
    public CustomResult GetBestPackageHealthCare() {
        try {
            return new CustomResult(200, null, _CustomerPackageRepo.GetBestPackageHealthCare());
        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }

    }

    @Override
    public CustomResult GetBestPackageLabTest() {
        try {
            return new CustomResult(200, null, _CustomerLabTestRepo.GetBestPackageLabTest());
        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResult GetBestPackageVaccination() {
        try {
            return new CustomResult(200, null, _CustomerVaccinationRepo.GetBestPackageVaccination());
        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResult GetDetailPackage(String attributeId, String id) {
        try {

            if (attributeId.equals("2")) {

                return new CustomResult(200, "Success", _CustomerPackageRepo.GetDetailPackage(Long.parseLong(id)));
            } else if (attributeId.equals("3")) {
                return new CustomResult(200, "Success", _CustomerLabTestRepo.GetDetailPackage(Long.parseLong(id)));

            } else {
                return new CustomResult(200, "Success", _CustomerVaccinationRepo.GetDetailPackage(Long.parseLong(id)));

            }

        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResult GetPackageByHospital(String id, int pageNo, int attributeId, String searchValue,
            String filterHospitalName) {
        try {
            Hospital h = null;
            List<String> filterHospitalNameList = null;
            if (filterHospitalName != null && !filterHospitalName.equals("[]")) {
                filterHospitalNameList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(filterHospitalName);
                for (int i = 0; i < jsonArray.length(); i++) {
                    filterHospitalNameList.add(jsonArray.getString(i));
                }
            }

            if (id != null) {
                h = _CustomerHospitalRepo.findById(Long.parseLong(id)).get();
            }

            Pageable pageable = PageRequest.of(pageNo, 4);
            if (attributeId == 2) {
                return new CustomResult(200, "Success",
                        _CustomerPackageRepo.GetPackageHealthCareByHospital(h, pageable, searchValue,
                                filterHospitalNameList));
            } else if (attributeId == 3) {
                return new CustomResult(200, "Success",
                        _CustomerLabTestRepo.GetPackageTestByHospital(h, pageable, searchValue,
                                filterHospitalNameList));
            } else {
                return new CustomResult(200, "Success",
                        _CustomerVaccinationRepo.GetPackageVaccnieByHospital(h, pageable, searchValue,
                                filterHospitalNameList));
            }
        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResult getPackageSuggestion(String id, int attributeId) {

        try {
            Hospital h = null;
            if (id != null) {
                h = _CustomerHospitalRepo.findById(Long.parseLong(id)).get();
            }
            if (attributeId == 2) {
                return new CustomResult(200, "Success", _CustomerPackageRepo.GetPackDTOLiteSuggess(h));
            } else if (attributeId == 3) {
                return new CustomResult(200, "Success", _CustomerLabTestRepo.GetPackDTOLiteSuggess(h));
            } else {
                return new CustomResult(200, "Success", _CustomerVaccinationRepo.GetPackDTOLiteSuggess(h));
            }

        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResult searchAllPack(int pageNo, String searchValue) {
        try {
            Pageable pageable = PageRequest.of(pageNo, 2);
            List<PackDTOLiteSearchAll> dataResult = new ArrayList<>();

            // Lấy dữ liệu từ _CustomerPackageRepo và ánh xạ
            var dataPackHealCare = _CustomerPackageRepo.searchAllHealCare(pageable, searchValue);
            List<PackDTOLiteSearchAll> dataHealCareList = dataPackHealCare.getContent().stream()
                    .map(pack -> new PackDTOLiteSearchAll(
                            pack.getId(),
                            pack.getHospitalId(),
                            pack.getName(),
                            pack.getFee(),
                            pack.getWorkDay(),
                            pack.getAvatar(),
                            pack.getHospitalName(), 2))
                    .collect(Collectors.toList());
            dataResult.addAll(dataHealCareList);

            // Lấy dữ liệu từ _CustomerLabTestRepo và ánh xạ
            var dataPackLab = _CustomerLabTestRepo.searchAllLabTest(pageable, searchValue);
            List<PackDTOLiteSearchAll> dataLabList = dataPackLab.getContent().stream()
                    .map(pack -> new PackDTOLiteSearchAll(
                            pack.getId(),
                            pack.getHospitalId(),
                            pack.getName(),
                            pack.getFee(),
                            pack.getWorkDay(),
                            pack.getAvatar(),
                            pack.getHospitalName(), 3))
                    .collect(Collectors.toList());
            dataResult.addAll(dataLabList);

            // Lấy dữ liệu từ _CustomerVaccinationRepo và ánh xạ
            var dataPackVaccine = _CustomerVaccinationRepo.searchAllVaccine(pageable, searchValue);
            List<PackDTOLiteSearchAll> dataVaccineList = dataPackVaccine.getContent().stream()
                    .map(pack -> new PackDTOLiteSearchAll(
                            pack.getId(),
                            pack.getHospitalId(),
                            pack.getName(),
                            pack.getFee(),
                            pack.getWorkDay(),
                            pack.getAvatar(),
                            pack.getHospitalName(), 4))
                    .collect(Collectors.toList());
            dataResult.addAll(dataVaccineList);

            return new CustomResult(200, "Success", dataResult);

        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class PackDTOLiteSearchAll {
        private Long id;
        private Long hospitalId;
        private String name;
        private Double fee;
        private String workDay;
        private String avatar;
        private String hospitalName;
        private int attributeId;
    }
}
