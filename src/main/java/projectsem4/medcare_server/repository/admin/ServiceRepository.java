package projectsem4.medcare_server.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.Service;

import projectsem4.medcare_server.interfaces.admin.IServices.ServicesRes;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IServices$ServicesRes(" +
      "s.id,s.hospital.id,s.attributes.id, s.status, s.attributes.name)" +
      "FROM Service s")
  List<ServicesRes> GetAll();

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IServices$ServicesRes(" +
      "s.id, s.hospital.id, s.attributes.id, s.status, s.attributes.name) " +
      "FROM Service s " +
      "WHERE (:code IS NULL OR :code = '' OR LOWER(s.hospital.code) LIKE LOWER(:code))")
  List<ServicesRes> GetByHospital(@Param("code") String code);
}
