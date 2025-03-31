package projectsem4.medcare_server.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

import projectsem4.medcare_server.domain.entity.Doctor;
import projectsem4.medcare_server.interfaces.admin.IDoctor.DoctorRes;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IDoctor$DoctorRes(" +
                        "d.id, d.code, d.name, d.workDay, d.timeWork, d.level, d.gender, " +
                        "d.fee, d.room, d.patients, d.avatar, d.createDate, d.updateDate, " +
                        "d.status, d.hospital.code, d.department.code, d.hospital.name,d.department.name,d.department.id, d.user.id) "
                        +
                        "FROM Doctor d WHERE " +
                        "(:codehospital IS NULL OR :codehospital = '' OR LOWER(d.hospital.code) LIKE LOWER(:codehospital)) AND "
                        +
                        "(:codedepartment IS NULL OR :codedepartment = '' OR LOWER(d.department.code) LIKE LOWER(:codedepartment)) AND "
                        +
                        "(:gender IS NULL OR :gender = '' OR LOWER(d.gender) LIKE LOWER(:gender)) AND " +
                        "(:level IS NULL OR :level = '' OR LOWER(d.level) LIKE LOWER(:level)) AND " +
                        "(:search IS NULL OR :search = '' OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%'))OR LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%'))) AND"
                        +
                        "(:feeStart IS NULL OR :feeEnd IS NULL OR (d.fee > :feeStart AND d.fee <= :feeEnd)) AND " +
                        "(:timeWork IS NULL OR :timeWork = '' OR LOWER(d.timeWork) LIKE LOWER(:timeWork))")
        Page<DoctorRes> findByFilters(
                        @Param("codehospital") String codehospital,
                        @Param("codedepartment") String codedepartment,
                        @Param("gender") String gender,
                        @Param("level") String level,
                        @Param("search") String search,
                        @Param("feeStart") Integer feeStart,
                        @Param("feeEnd") Integer feeEnd,
                        @Param("timeWork") String timeWork,
                        Pageable pageable);

        List<Doctor> findByCode(String code);

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IDoctor$DoctorRes(" +
                        "d.id, d.code, d.name, d.workDay, d.timeWork, d.level, d.gender, " +
                        "d.fee, d.room, d.patients, d.avatar, d.createDate, d.updateDate, " +
                        "d.status, d.hospital.code, d.department.code, d.hospital.name,d.department.name,d.department.id, d.user.id) "
                        +
                        "FROM Doctor d WHERE " +
                        "d.hospital.id = :id")
        List<DoctorRes> findByHospital(
                        @Param("id") Long id);
}
