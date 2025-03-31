package projectsem4.medcare_server.repository.admin;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.EventVaccine;
import projectsem4.medcare_server.domain.entity.HospitalEvent;
import projectsem4.medcare_server.interfaces.admin.IChart.TopHospitalRespon;
import projectsem4.medcare_server.interfaces.admin.IHospitalEvent.HospitalEventRes;

@Repository
public interface HospitalEventRepository extends JpaRepository<HospitalEvent, Long> {

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IHospitalEvent$HospitalEventRes(" +
      "b.id, b.createDate,b.updateDate,b.status, b.vaccine.hospital.id,b.vaccine.hospital.name,b.vaccine.hospital.code,"
      +
      "b.vaccine.id,b.vaccine.name,b.vaccine.code,b.vaccine.fee,b.vaccine.status) " +
      "FROM HospitalEvent b WHERE " +
      "(:idHospital IS NULL OR b.vaccine.hospital.id = :idHospital) AND " +
      "(:idEvent IS NULL OR b.eventVaccine.id = :idEvent)")
  // "GROUP BY(b.vaccine.hospital.id)")
  List<HospitalEventRes> hospitalInEvent(
      @Param("idHospital") Long idHospital,
      @Param("idEvent") Long idEvent);

  List<HospitalEvent> findByEventVaccine(EventVaccine eventVaccine);
}
