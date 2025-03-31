package projectsem4.medcare_server.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import projectsem4.medcare_server.domain.entity.CodeVerify;

public interface CodeVerifyRepo extends JpaRepository<CodeVerify, Long> {
    CodeVerify findByEmail(String email);

}
