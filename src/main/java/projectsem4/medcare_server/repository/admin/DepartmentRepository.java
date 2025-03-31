package projectsem4.medcare_server.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.Department;
import projectsem4.medcare_server.interfaces.admin.IDepartment.DepartmentRes;
import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IDepartment$DepartmentRes(" +
                        "d.id, d.code, d.name, d.floor, d.zone, d.createDate, d.updateDate, d.status," +
                        "d.hospital.name, d.hospital.code, d.user.id, COUNT(do.id)) " +
                        "FROM Department d " +
                        "LEFT JOIN  d.doctors do " +
                        "WHERE (:status IS NULL OR :status = '' OR LOWER(d.status) LIKE LOWER(:status)) AND " +
                        "(:codehospital IS NULL OR :codehospital = '' OR LOWER(d.hospital.code) LIKE LOWER(:codehospital)) AND "
                        +
                        "(:search IS NULL OR :search = '' OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%'))) "
                        +
                        "GROUP BY d.id, d.code, d.name, d.floor, d.zone, d.createDate, d.updateDate, d.status, " +
                        "d.hospital.name, d.hospital.code, d.user.id")
        Page<DepartmentRes> findByFilters(
                        @Param("status") String status,
                        @Param("codehospital") String codehospital,
                        @Param("search") String search,
                        Pageable pageable);

        List<Department> findByCode(String code);
}
