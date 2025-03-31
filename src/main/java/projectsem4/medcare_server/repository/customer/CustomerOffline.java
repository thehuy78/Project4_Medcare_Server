package projectsem4.medcare_server.repository.customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.entity.Doctor;
import projectsem4.medcare_server.domain.entity.Offline;
import projectsem4.medcare_server.domain.entity.User;

public interface CustomerOffline extends JpaRepository<Offline, Long> {

    @Query(" SELECT new projectsem4.medcare_server.repository.customer.CustomerOffline$OfflineDTO(n.id,n.day ) FROM Offline n WHERE n.status = 'active' AND n.doctor =:doctor ")
    List<OfflineDTO> findByDoctorCustom(@Param("doctor") Doctor doctor);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class OfflineDTO {
        Long id;
        String day;
    }
}
