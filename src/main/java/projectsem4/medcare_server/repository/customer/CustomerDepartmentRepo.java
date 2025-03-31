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
import projectsem4.medcare_server.domain.entity.Hospital;

import java.util.List;

public interface CustomerDepartmentRepo extends JpaRepository<Department, Long> {

        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerDepartmentRepo$DepartmentLite("
                        +
                        "h.id, h.name  )" +
                        "FROM Department h  " +
                        "WHERE h.status = 'active' AND h.hospital = :id ")
        List<DepartmentLite> findByHospital(@Param("id") Hospital id);

        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerDepartmentRepo$DepartmentLite("
                        +
                        "h.id, h.name  )" +
                        "FROM Department h " +
                        "WHERE h.status = 'active'")
        List<DepartmentLite> GetAllCustom();

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        class DepartmentLite {
                Long id;
                String name;
        }

        @Query("SELECT DISTINCT new projectsem4.medcare_server.repository.customer.CustomerDepartmentRepo$DepartmentWithHospitalName("
                        +
                        "h.id, h.name,p.logo,p.name  )" +
                        "FROM Department h join h.hospital p LEFT JOIN h.doctors d " +
                        "WHERE h.status = 'active' AND (:searchValue IS NULL OR h.name ILIKE '%' || :searchValue || '%'"
                        // +
                        // " OR d.name ILIKE '%' || :searchValue || '%' OR p.name ILIKE '%' ||
                        // :searchValue || '%' "
                        +
                        ")  ")
        Page<DepartmentWithHospitalName> DepartmentWithHospitalName(@Param("searchValue") String searchValue,
                        Pageable pageable);

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        class DepartmentWithHospitalName {
                Long id;
                String name;
                String logo;
                String hospitalName;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class DetailFacitlityDTO {
                private Long id;
                private String name;
                private String address;
                private String logo;
                private String image;
                private String description;
                private String dayWork;
                private String openTime;
                private String closeTime;
                private Long type;
                private String province;
                private String district;

        }

        @Query("SELECT DISTINCT new projectsem4.medcare_server.repository.customer.CustomerDepartmentRepo$DetailFacitlityDTO("
                        +
                        "h.id, h.name, h.address, h.logo, h.image, h.description, h.workDay, h.openTime, h.closeTime, t.id, "
                        +
                        "CASE WHEN h.province LIKE '%Thành phố%' THEN REPLACE(h.province, 'Thành phố ', 'TP.') ELSE h.province END, "
                        +
                        "CASE WHEN h.district LIKE '%Quận%' THEN REPLACE(h.district, 'Quận ', 'Q.') " +
                        "WHEN h.district LIKE '%Thành phố%' THEN REPLACE(h.district, 'Thành phố ', 'TP.') ELSE h.district END) "
                        +
                        "FROM Department d JOIN d.hospital h JOIN h.type t " +
                        "WHERE h.status = 'active' " +
                        "AND (:searchValue IS NULL OR h.name ILIKE '%' || :searchValue || '%' OR d.name ILIKE '%' || :searchValue || '%')")
        Page<DetailFacitlityDTO> searchAllHospital(Pageable pageable, @Param("searchValue") String searchValue);
}
