package projectsem4.medcare_server.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.Offline;
import projectsem4.medcare_server.interfaces.admin.IOffline.OfflineRes;
import projectsem4.medcare_server.interfaces.admin.ITesting.TestingRes;

@Repository
public interface OfflineRepository extends JpaRepository<Offline, Long> {
    // @Query("SELECT new
    // projectsem4.medcare_server.interfaces.admin.IOffline$OfflineRes(" +
    // "d.id, d.day, d.description, d.status,
    // d.user.email,d.doctor.code,d.doctor.name, d.doctor.avatar, d.createDate,
    // d.updateDate) "
    // +
    // "FROM Offline d " +
    // "WHERE (:status IS NULL OR :status = '' OR LOWER(d.status) LIKE
    // LOWER(:status)) AND "
    // +
    // "(:hospital IS NULL OR d.hospital.id = :hospital) AND" +
    // "(:search IS NULL OR :search = '' OR LOWER(d.doctor.name) LIKE
    // LOWER(CONCAT('%', :search, '%')) OR LOWER(d.doctor.code) LIKE
    // LOWER(CONCAT('%', :search, '%'))) ")
    // Page<OfflineRes> findByFilters(
    // @Param("status") String status,
    // @Param("search") String search,
    // @Param("hospital") Long hospital,
    // Pageable pageable);

    @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IOffline$OfflineRes(" +
            "d.id, d.day, d.description, d.status, d.user.email, d.doctor.code, d.doctor.name, d.doctor.avatar, d.createDate, d.updateDate) "
            +
            "FROM Offline d " +
            "WHERE (:status IS NULL OR :status = '' OR LOWER(d.status) LIKE LOWER(:status)) AND " +
            "(:hospital IS NULL OR d.hospital.id = :hospital) AND " +
            "(:search IS NULL OR :search = '' OR LOWER(d.doctor.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(d.doctor.code) LIKE LOWER(CONCAT('%', :search, '%'))) AND "
            +
            "DATEDIFF(CURRENT_DATE, d.createDate) <= 90 " +
            "ORDER BY d.createDate DESC")
    Page<OfflineRes> findByFilters(
            @Param("status") String status,
            @Param("search") String search,
            @Param("hospital") Long hospital,
            Pageable pageable);
}
