package projectsem4.medcare_server.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.Pack;

import projectsem4.medcare_server.interfaces.admin.IPack.PackRes;
import java.util.List;

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IPack$PackRes(" +
                        "d.id, d.code, d.name, d.description, d.fee, d.createDate, d.updateDate, d.status," +
                        "d.hospital.name, d.hospital.code, d.user.email) " +
                        "FROM Pack d " +
                        "WHERE (:status IS NULL OR :status = '' OR LOWER(d.status) LIKE LOWER(:status)) AND " +
                        "(:codehospital IS NULL OR :codehospital = '' OR LOWER(d.hospital.code) LIKE LOWER(:codehospital)) AND "
                        +
                        "(:feeStart IS NULL OR :feeEnd IS NULL OR (d.fee > :feeStart AND d.fee <= :feeEnd)) AND " +
                        "(:search IS NULL OR :search = '' OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%'))OR LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%'))) ")
        Page<PackRes> findByFilters(
                        @Param("status") String status,
                        @Param("codehospital") String codehospital,
                        @Param("search") String search,
                        @Param("feeStart") Double feeStart,
                        @Param("feeEnd") Double feeEnd,
                        Pageable pageable);

        List<Pack> findByCode(String code);
}
