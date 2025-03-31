package projectsem4.medcare_server.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import projectsem4.medcare_server.domain.entity.Hospital;
import projectsem4.medcare_server.interfaces.admin.IHospital.HospitalRes;
import projectsem4.medcare_server.interfaces.admin.IHospital.HospitalResNameAndCode;
import projectsem4.medcare_server.interfaces.admin.IHospital.HospitalResNameAndCodeAndId;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IHospital$HospitalRes(" +
                        "d.id, d.code, d.name, d.logo, d.image, d.district, d.province, " +
                        "d.openTime, d.closeTime, d.workDay, d.address, d.map, d.description, " +
                        "d.createDate, d.updateDate, d.status, d.type.name) " +
                        "FROM Hospital d WHERE " +
                        "(:type IS NULL OR :type = '' OR LOWER(d.type.name) LIKE LOWER(:type)) AND " +
                        "(:status IS NULL OR :status = '' OR LOWER(d.status) LIKE LOWER(:status)) AND " +
                        "(:address IS NULL OR :address = '' OR LOWER(d.district) LIKE LOWER(:address)) AND " +
                        "(:search IS NULL OR :search = '' OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%')))")
        Page<HospitalRes> findByFilters(
                        @Param("type") String type,
                        @Param("status") String status,
                        @Param("address") String address,
                        @Param("search") String search,
                        Pageable pageable);

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IHospital$HospitalResNameAndCode(" +
                        "d.code,d.name)" +
                        "FROM Hospital d")
        List<HospitalResNameAndCode> getNameAndId();

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IHospital$HospitalResNameAndCodeAndId(" +
                        "d.id, d.code, d.name) " + // Added a space after the closing parenthesis
                        "FROM Hospital d " + // Added a space after `Hospital d`
                        "WHERE " +
                        "(:search IS NULL OR :search = '' OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                        "OR LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%')))")
        Page<HospitalResNameAndCodeAndId> getNameAndCodeAndId(
                        @Param("search") String search,
                        Pageable pageable);

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IHospital$HospitalResNameAndCodeAndId(" +
                        "d.id, d.code, d.name) " + // Added a space after the closing parenthesis
                        "FROM Hospital d")
        List<HospitalResNameAndCodeAndId> getNameAndCodeAndIdList();

        List<Hospital> findByCode(String code);

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IHospital$HospitalRes(" +
                        "d.id, d.code, d.name, d.logo, d.image, d.district, d.province, " +
                        "d.openTime, d.closeTime, d.workDay, d.address, d.map, d.description, " +
                        "d.createDate, d.updateDate, d.status, d.type.name) " +
                        "FROM Hospital d WHERE d.id = :id")
        List<HospitalRes> getbyId(
                        @Param("id") Long id);

}
