package projectsem4.medcare_server.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import projectsem4.medcare_server.domain.entity.Bill;

public interface CustomerBillRepo extends JpaRepository<Bill, Long> {

}
