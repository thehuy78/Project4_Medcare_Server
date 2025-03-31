package projectsem4.medcare_server.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import projectsem4.medcare_server.domain.entity.Transaction;

public interface CustomerTransactionRepo extends JpaRepository<Transaction, Long> {

}
