package projectsem4.medcare_server.repository.customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.entity.Service;

public interface CustomerHospitalServiceRepo extends JpaRepository<Service, Long> {

    @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerHospitalServiceRepo$HospitalServiceDTO(a.id, a.name) FROM Service s join s.attributes a join s.hospital h where h.id =:id AND s.status = 'active'")
    List<HospitalServiceDTO> GetService(@Param("id") Long id);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class HospitalServiceDTO {
        private Long id;
        private String name;

    }
}
