package projectsem4.medcare_server.repository.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.entity.Department;
import projectsem4.medcare_server.domain.entity.Doctor;
import projectsem4.medcare_server.domain.entity.Hospital;

import java.util.Date;
import java.util.List;

public interface CustomerDoctorRepo extends JpaRepository<Doctor, Long> {
        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerDoctorRepo$DoctorDTOFindByDepartment("
                        +
                        "h.id, h.name, h.avatar, h.level,h.fee,h.room,h.workDay,h.timeWork, d.id, o.day  ,COUNT(p) as countBooking )  "
                        +
                        "FROM Doctor h join h.hospital d LEFT JOIN h.Offlines o  LEFT JOIN h.bookings p ON p.bookingDate = :dateBooking  "
                        +
                        "WHERE h.status = 'active' AND h.department = :id AND (o.status IS NULL OR o.status='active') "
                        +
                        "GROUP BY h.id, h.name, h.avatar, h.level, h.fee, h.room , h.patients , o.day"

        )
        List<DoctorDTOFindByDepartment> findByDepartment(@Param("id") Department id,
                        @Param("dateBooking") Date dateBooking);

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class DoctorDTOFindByDepartment {
                Long id;
                String name;
                String avatar;
                String level;
                Double fee;
                Integer room;
                String workDay;
                String workTime;
                Long hospitalId;
                String day;
                Long countBooking;

        }

        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerDoctorRepo$DoctorDTOFindByHospital("
                        +
                        "h.id, h.name, h.avatar, h.level,h.fee,h.room,h.workDay,h.timeWork,d.name, p.name,p.id )  "
                        +
                        "FROM Doctor h join h.department d join h.hospital p " +
                        "WHERE h.status = 'active' AND (:id IS NULL OR h.hospital = :id ) AND (:searchValue IS NULL OR h.name ILIKE '%' || :searchValue || '%' ) "
                        +
                        " AND (:departmentName IS NULL OR d.name IN :departmentName ) AND (:level IS NULL OR h.level IN :level ) AND (:gender IS NULL OR h.gender IN :gender ) AND (:ListHospitalName IS NULL OR p.name IN :ListHospitalName )  ")
        Page<DoctorDTOFindByHospital> getDoctorDTOFindByHospitalFilter(@Param("id") Hospital id, Pageable pageable,
                        @Param("searchValue") String searchValue, @Param("departmentName") List<String> departmentName,
                        @Param("level") List<String> level, @Param("gender") List<String> gender,
                        @Param("ListHospitalName") List<String> ListHospitalName);

        @Query("SELECT DISTINCT new projectsem4.medcare_server.repository.customer.CustomerDoctorRepo$DoctorDTOFindByHospital("
                        +
                        "h.id, h.name, h.avatar, h.level,h.fee,h.room,h.workDay,h.timeWork,d.name, p.name,p.id )  "
                        +
                        "FROM Doctor h join h.department d join h.hospital p " +
                        "WHERE h.status = 'active' AND (:searchValue IS NULL OR h.name ILIKE '%' || :searchValue || '%' "
                        // +
                        // " OR d.name ILIKE '%' || :searchValue || '%' OR p.name ILIKE '%' ||
                        // :searchValue || '%' "
                        +
                        ") "

        )
        Page<DoctorDTOFindByHospital> getDoctorDTOFindByHospitalAndDepartment(
                        Pageable pageable,
                        @Param("searchValue") String searchValue);

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class DoctorDTOFindByHospital {
                Long id;
                String name;
                String avatar;
                String level;
                Double fee;
                Integer room;
                String workDay;
                String workTime;
                String departmentName;
                String hospitalName;
                Long hospitalId;
        }

        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerDoctorRepo$DoctorDTOFindByHospitalLite("
                        +
                        "h.id, h.name )  "
                        +
                        "FROM Doctor h  " +
                        "WHERE h.status = 'active' AND (:id IS NULL OR h.hospital = :id ) "
                        +
                        "GROUP BY h.id, h.name, h.avatar, h.level, h.fee, h.room , h.patients "

        )

        List<DoctorDTOFindByHospitalLite> DoctorDTOFindByHospitalLite(@Param("id") Hospital id);

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class DoctorDTOFindByHospitalLite {
                Long id;
                String name;

        }

}
