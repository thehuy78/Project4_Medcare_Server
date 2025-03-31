package projectsem4.medcare_server.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;
import projectsem4.medcare_server.domain.entity.Type;
import projectsem4.medcare_server.interfaces.admin.IType.TypeDto;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

  @Query("SELECT NEW projectsem4.medcare_server.interfaces.admin.IType$TypeDto(" +
      "t.id, t.name )" +
      "FROM Type t " +
      "WHERE t.type = 'booking' " +
      "ORDER BY t.id")
  List<TypeDto> GetTypeBooking();

  @Query("SELECT NEW projectsem4.medcare_server.interfaces.admin.IType$TypeDto(" +
      "t.id, t.name )" +
      "FROM Type t " +
      "WHERE t.type = 'hospital' " +
      "ORDER BY t.id")
  List<TypeDto> GetTypeHospital();
}
