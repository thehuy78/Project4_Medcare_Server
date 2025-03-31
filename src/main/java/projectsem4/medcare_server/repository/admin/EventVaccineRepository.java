package projectsem4.medcare_server.repository.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.EventVaccine;
import projectsem4.medcare_server.domain.entity.Pack;
import projectsem4.medcare_server.interfaces.admin.IEventVaccine.EventVaccineRes;

@Repository
public interface EventVaccineRepository extends JpaRepository<EventVaccine, Long> {

    @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IEventVaccine$EventVaccineRes(" +
            "e.id, e.code, e.name,e.createDate,e.updateDate, "
            +
            "e.status,e.description,e.user.email) "
            +
            "FROM EventVaccine e WHERE " +
            "(:status IS NULL OR :status = '' OR LOWER(e.status) LIKE LOWER(:status)) AND " +
            "(:search IS NULL OR :search = '' OR LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(e.code) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<EventVaccineRes> findAllByFillter(
            @Param("status") String status,
            @Param("search") String search,
            Pageable pageable);

    List<EventVaccine> findByCode(String code);
}
