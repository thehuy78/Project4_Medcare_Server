package projectsem4.medcare_server.repository.customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.entity.Type;

public interface CustomerTypeRepo extends JpaRepository<Type, Long> {

    @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerTypeRepo$TypeDTO(h.id, h.name) FROM Type h WHERE h.type = 'hospital' ")
    List<TypeDTO> GetTypeCustom();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class TypeDTO {
        Long id;
        String name;

    }
}
