package projectsem4.medcare_server.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.Vaccine;

import projectsem4.medcare_server.interfaces.admin.IVaccine.VaccineRes;
import java.util.List;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IVaccine$VaccineRes(" +
                        "d.id, d.code, d.name, d.description, d.fee,d.floor,d.zone, d.createDate, d.updateDate, d.status,"
                        +
                        "d.hospital.name, d.hospital.code, d.user.email) " +
                        "FROM Vaccine d " +
                        "WHERE (:status IS NULL OR :status = '' OR LOWER(d.status) LIKE LOWER(:status)) AND " +
                        "(:codehospital IS NULL OR :codehospital = '' OR LOWER(d.hospital.code) LIKE LOWER(:codehospital)) AND "
                        +
                        "(:feeStart IS NULL OR :feeEnd IS NULL OR (d.fee > :feeStart AND d.fee <= :feeEnd)) AND " +
                        "(:search IS NULL OR :search = '' OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%'))) ")
        Page<VaccineRes> findByFilters(
                        @Param("status") String status,
                        @Param("codehospital") String codehospital,
                        @Param("search") String search,
                        @Param("feeStart") Double feeStart,
                        @Param("feeEnd") Double feeEnd,
                        Pageable pageable);

        List<Vaccine> findByCode(String code);
}
