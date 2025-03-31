package projectsem4.medcare_server.repository.customer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.entity.Hospital;
import projectsem4.medcare_server.domain.entity.Pack;

public interface CustomerPackageRepo extends JpaRepository<Pack, Long> {

    @Query(" SELECT new projectsem4.medcare_server.repository.customer.CustomerPackageRepo$BestPackDTO(p.id,h.id,h.workDay ,p.name, h.image,h.name,p.fee ) FROM Pack p join p.hospital h LEFT JOIN p.bookings b WHERE p.status = 'active' AND b.pack IS NOT NULL GROUP BY p.id,h.id,h.workDay ,p.name, h.image,h.name,p.fee ORDER BY COUNT(b) DESC LIMIT 5 ")
    List<BestPackDTO> GetBestPackageHealthCare();

    @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerPackageRepo$DetailPackageCustomerDTO(" +
            "p.id, h.id, p.name, h.address, h.image,h.name,h.workDay,p.fee,p.description, " +
            "CASE WHEN h.province LIKE '%Thành phố%' THEN REPLACE(h.province, 'Thành phố ', 'TP.') " +
            "ELSE h.province END , " +
            "CASE WHEN h.district LIKE '%Quận%' THEN REPLACE(h.district, 'Quận ', 'Q.') " +
            "WHEN h.district LIKE '%Thành phố%' THEN REPLACE(h.district, 'Thành phố ', 'TP.') " +
            "WHEN h.district LIKE '%Thành Phố%' THEN REPLACE(h.district, 'Thành Phố ', 'TP.') " +
            "ELSE h.district END) " +
            "FROM Pack p join p.hospital h WHERE p.status = 'active' AND p.id =:id ")
    DetailPackageCustomerDTO GetDetailPackage(@Param("id") Long id);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class BestPackDTO {
        private Long id;
        private Long hospitalId;
        private String dateWork;
        private String name;
        private String logo;
        private String hospitalName;
        private Double fee;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class DetailPackageCustomerDTO {
        private Long id;
        private Long hospitalId;
        private String name;
        private String address;
        private String hospitalImage;
        private String hospitalName;
        private String dateWork;
        private Double fee;
        private String description;
        private String province;
        private String district;

    }

    @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerPackageRepo$PackDTOLite(p.id,h.id, p.name,p.fee,h.workDay,h.logo,h.name ) FROM Pack p join p.hospital h WHERE p.status = 'active' AND (:hospital IS NULL OR p.hospital =:hospital ) AND (:searchValue IS NULL OR p.name ILIKE '%' || :searchValue || '%' ) AND (:filterHospitalName IS NULL OR h.name IN :filterHospitalName ) ")
    Page<PackDTOLite> GetPackageHealthCareByHospital(@Param("hospital") Hospital hospital, Pageable pageable,
            @Param("searchValue") String searchValue, @Param("filterHospitalName") List<String> filterHospitalName);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class PackDTOLite {
        private Long id;
        private Long hospitalId;
        private String name;
        private Double fee;
        private String workDay;
        private String avatar;
        private String hospitalName;

    }

    @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerPackageRepo$PackDTOLiteSuggess(p.id,p.name ) FROM Pack p  WHERE p.status = 'active' AND (:hospital IS NULL OR p.hospital =:hospital )")
    List<PackDTOLiteSuggess> GetPackDTOLiteSuggess(@Param("hospital") Hospital hospital);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class PackDTOLiteSuggess {
        private Long id;
        private String name;
    }

    @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerPackageRepo$PackDTOLite(p.id,h.id, p.name,p.fee,h.workDay,h.logo,h.name ) FROM Pack p join p.hospital h WHERE p.status = 'active' AND (:searchValue IS NULL OR p.name ILIKE '%' || :searchValue || '%' )")
    Page<PackDTOLite> searchAllHealCare(Pageable pageable, @Param("searchValue") String searchValue);

}
