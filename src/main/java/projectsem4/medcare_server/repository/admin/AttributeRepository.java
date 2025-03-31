package projectsem4.medcare_server.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.Attributes;

@Repository
public interface AttributeRepository extends JpaRepository<Attributes, Long> {

  @Query("SELECT a FROM Attributes a WHERE a.type LIKE '%service%'")
  List<Attributes> GetAttributeService();
}
