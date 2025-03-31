package projectsem4.medcare_server.repository.admin;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.CodeVerify;

@Repository
public interface CodeVerifyRepository extends JpaRepository<CodeVerify, Long> {

  CodeVerify findByEmail(String email);

  @Modifying
  @Query("DELETE FROM CodeVerify c WHERE c.expDate < :currentDate")
  int deleteByExpDateBefore(Date currentDate);
}
