package projectsem4.medcare_server.service.customer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.checkerframework.checker.units.qual.h;
import org.json.JSONArray;
import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Department;
import projectsem4.medcare_server.domain.entity.Doctor;
import projectsem4.medcare_server.domain.entity.Hospital;
import projectsem4.medcare_server.interfaces.customer.ICustomerDoctor;
import projectsem4.medcare_server.repository.customer.CustomerDepartmentRepo;
import projectsem4.medcare_server.repository.customer.CustomerDoctorRepo;
import projectsem4.medcare_server.repository.customer.CustomerHospitalRepo;
import projectsem4.medcare_server.repository.customer.CustomerOffline;

@Service
public class CustomerDoctorService implements ICustomerDoctor {

    @Autowired
    CustomerDoctorRepo _CustomerDoctorRepo;

    @Autowired
    CustomerDepartmentRepo _CustomerDepartmentRepo;

    @Autowired
    CustomerHospitalRepo _CustomerHospitalRepo;
    @Autowired
    CustomerOffline _CustomerOffline;

    @Override
    public CustomResult getByDepartMentId(String id, String dateBooking) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            LocalDateTime localDate = LocalDateTime
                    .parse(dateBooking.substring(0, dateBooking.length() - 1), formatter);
            Date date = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
            return new CustomResult(200, "Success",
                    _CustomerDoctorRepo.findByDepartment(_CustomerDepartmentRepo.findById(Long.parseLong(id)).get(),
                            date));
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult getByHospitalFilter(String id, int pageNo, String searchValue, String filterOption) {
        try {
            JSONObject jsonObject = new JSONObject(filterOption);
            List<String> departmentNameFilter = null;
            if (!jsonObject.isNull("departmentName")) {
                JSONArray departmentNameArray = jsonObject.getJSONArray("departmentName");
                if (departmentNameArray.length() > 0) {
                    departmentNameFilter = new ArrayList<>();
                    for (int i = 0; i < departmentNameArray.length(); i++) {
                        departmentNameFilter.add(departmentNameArray.getString(i));
                    }
                }

            }
            List<String> levelFilter = null;
            if (!jsonObject.isNull("level")) {
                JSONArray levelArray = jsonObject.getJSONArray("level");
                if (levelArray.length() > 0) {
                    levelFilter = new ArrayList<>();
                    for (int i = 0; i < levelArray.length(); i++) {
                        levelFilter.add(levelArray.getString(i));
                    }
                }
            }
            List<String> genderFilter = null;
            if (!jsonObject.isNull("gender")) {
                JSONArray genderArray = jsonObject.getJSONArray("gender");
                if (genderArray.length() > 0) {
                    genderFilter = new ArrayList<>();
                    for (int i = 0; i < genderArray.length(); i++) {
                        if (genderArray.getInt(i) == 0) {
                            genderFilter.add("Male");
                        } else {
                            genderFilter.add("Female");
                        }

                    }
                }
            }

            if (id != null) {
                Hospital hospital = _CustomerHospitalRepo.findById(Long.parseLong(id)).get();
                Pageable pageable = PageRequest.of(pageNo, 4);
                return new CustomResult(200, "Success",
                        _CustomerDoctorRepo.getDoctorDTOFindByHospitalFilter(hospital, pageable, searchValue,
                                departmentNameFilter, levelFilter,
                                genderFilter, null));
            } else {
                List<String> hospitalNameFilter = null;
                if (!jsonObject.isNull("hospitalName")) {
                    JSONArray hospitalNameArray = jsonObject.getJSONArray("hospitalName");
                    if (hospitalNameArray.length() > 0) {
                        hospitalNameFilter = new ArrayList<>();
                        for (int i = 0; i < hospitalNameArray.length(); i++) {
                            hospitalNameFilter.add(hospitalNameArray.getString(i));
                        }
                    }

                }
                Pageable pageable = PageRequest.of(pageNo, 4);
                return new CustomResult(200, "Success",
                        _CustomerDoctorRepo.getDoctorDTOFindByHospitalFilter(null, pageable, searchValue,
                                departmentNameFilter, levelFilter,
                                genderFilter, hospitalNameFilter));
            }

        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    // id null thi tra ve tat ca
    @Override
    public CustomResult getByHospitalLite(String id) {
        try {
            Hospital hospital = null;
            if (id != null) {
                hospital = _CustomerHospitalRepo.findById(Long.parseLong(id)).get();
            }
            return new CustomResult(200, "Success",
                    _CustomerDoctorRepo.DoctorDTOFindByHospitalLite(hospital));
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult getDoctorBySearchAll(int pageNo, String searchValue) {
        try {

            Pageable pageable = PageRequest.of(pageNo, 4);
            return new CustomResult(200, "Success",
                    _CustomerDoctorRepo.getDoctorDTOFindByHospitalAndDepartment(pageable, searchValue));

        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult getOfflineDay(String doctorId) {
        try {
            Doctor d = _CustomerDoctorRepo.findById(Long.parseLong(doctorId)).get();
            return new CustomResult(200, "Success", _CustomerOffline.findByDoctorCustom(d));
        } catch (Exception e) {
            return new CustomResult(400, "Failed", null);
        }
    }

}
