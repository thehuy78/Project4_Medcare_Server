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
import projectsem4.medcare_server.repository.customer.CustomerDoctorRepo.DoctorDTOFindByHospital;

public interface CustomerHospitalRepo extends JpaRepository<Hospital, Long> {

        // Query lại các bệnh viện
        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerHospitalRepo$BestPartnerDTO(h.id, h.name, h.logo) FROM Hospital h WHERE h.status = 'active' ")
        List<BestPartnerDTO> GetBestPartner();

        // Query lại các đơn vị có đơn cao nhất
        // @Query("SELECT new
        // projectsem4.medcare_server.repository.customer.CustomerHospitalRepo$BestFacitlityDTO("
        // +
        // "h.id, h.name, h.address, h.image, " +
        // "CASE WHEN h.province LIKE '%Thành phố%' THEN REPLACE(h.province, 'Thành phố
        // ', 'TP.') " +
        // "ELSE h.province END , " +
        // "CASE WHEN h.district LIKE '%Quận%' THEN REPLACE(h.district, 'Quận ', 'Q.') "
        // +
        // "WHEN h.district LIKE '%Thành Phố%' THEN REPLACE(h.district, 'Thành Phố ',
        // 'TP.') " +
        // "ELSE h.district END) " +
        // "FROM Hospital h LEFT JOIN h.bookings b WHERE h.status = 'active' AND
        // b.hospital IS NOT NULL GROUP BY h.id, h.name, h.address, h.image , h.province
        // ,h.district ORDER BY COUNT(b) DESC LIMIT 5 ")
        // List<BestFacitlityDTO> GetBestFacility();

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class BestPartnerDTO {
                private Long id;
                private String name;
                private String logo;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class BestFacitlityDTO {
                private Long id;
                private String name;
                private String address;
                private String logo;
                private String province;
                private String district;

        }

        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerHospitalRepo$DetailFacitlityDTO(" +
                        "h.id, h.name, h.address, h.logo,h.image,h.description,h.workDay,h.openTime,h.closeTime,t.id ,"
                        +
                        "CASE WHEN h.province LIKE '%Thành phố%' THEN REPLACE(h.province, 'Thành phố ', 'TP.') " +
                        "ELSE h.province END , " +
                        "CASE WHEN h.district LIKE '%Quận%' THEN REPLACE(h.district, 'Quận ', 'Q.') " +
                        "WHEN h.district LIKE '%Thành phố%' THEN REPLACE(h.district, 'Thành phố ', 'TP.') " +
                        "ELSE h.district END) " +
                        "FROM Hospital h join h.type t WHERE h.status = 'active' AND h.id =:id ")
        DetailFacitlityDTO GetDetailFacility(@Param("id") Long id);

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

        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerHospitalRepo$BasicDTO(h.id, h.name ) FROM Hospital h WHERE h.status = 'active' ")
        List<BasicDTO> getHospitalSuggestion();

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class BasicDTO {
                private Long id;
                private String name;

        }

        @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerHospitalRepo$DetailFacitlityDTO(" +
                        "h.id, h.name, h.address, h.logo,h.image,h.description,h.workDay,h.openTime,h.closeTime,t.id ,"
                        +
                        "CASE WHEN h.province LIKE '%Thành phố%' THEN REPLACE(h.province, 'Thành phố ', 'TP.') " +
                        "ELSE h.province END , " +
                        "CASE WHEN h.district LIKE '%Quận%' THEN REPLACE(h.district, 'Quận ', 'Q.') " +
                        "WHEN h.district LIKE '%Thành phố%' THEN REPLACE(h.district, 'Thành phố ', 'TP.') " +
                        "ELSE h.district END) " +
                        "FROM Hospital h join h.type t WHERE h.status = 'active' AND (:searchValue IS NULL OR h.name ILIKE '%' || :searchValue || '%' ) AND (:typeId IS NULL OR t.id =:typeId ) ")
        Page<DetailFacitlityDTO> getAllCustom(Pageable pageable,
                        @Param("searchValue") String searchValue, @Param("typeId") Long typeId);

        // @Query("SELECT DISTINCT new
        // projectsem4.medcare_server.repository.customer.CustomerHospitalRepo$DetailFacitlityDTO("
        // +
        // "h.id, h.name, h.address,
        // h.logo,h.image,h.description,h.workDay,h.openTime,h.closeTime,t.id ,"
        // +
        // "CASE WHEN h.province LIKE '%Thành phố%' THEN REPLACE(h.province, 'Thành phố
        // ', 'TP.') " +
        // "ELSE h.province END , " +
        // "CASE WHEN h.district LIKE '%Quận%' THEN REPLACE(h.district, 'Quận ', 'Q.') "
        // +
        // "WHEN h.district LIKE '%Thành phố%' THEN REPLACE(h.district, 'Thành phố ',
        // 'TP.') " +
        // "ELSE h.district END) " +
        // "FROM Hospital h join h.type t LEFT JOIN h.departments d WHERE h.status =
        // 'active' AND (:searchValue IS NULL OR h.name ILIKE '%' || :searchValue || '%'
        // "
        // // +
        // // " OR d.name ILIKE '%' || :searchValue || '%' "
        // +
        // ")")
        // Page<DetailFacitlityDTO> searchAllHospital(Pageable pageable,
        // @Param("searchValue") String searchValue);

}