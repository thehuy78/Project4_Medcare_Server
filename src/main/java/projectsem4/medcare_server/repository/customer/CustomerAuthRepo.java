package projectsem4.medcare_server.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import projectsem4.medcare_server.domain.entity.User;

public interface CustomerAuthRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
