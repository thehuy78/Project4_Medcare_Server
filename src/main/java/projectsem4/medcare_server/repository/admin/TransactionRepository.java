package projectsem4.medcare_server.repository.admin;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.Test;
import projectsem4.medcare_server.domain.entity.Transaction;
import projectsem4.medcare_server.interfaces.admin.IBooking.BookingRes;
import projectsem4.medcare_server.interfaces.admin.ITesting.TestingRes;
import projectsem4.medcare_server.interfaces.admin.ITransaction.TransactionRes;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.ITransaction$TransactionRes(" +
      "d.id, d.traded, d.balance, d.description, d.createDate, d.updateDate, d.status, " +
      "d.user.email) " +
      "FROM Transaction d " +
      "WHERE (:type IS NULL OR :type = '' OR " +
      "( :type = 'positive' AND d.traded > 0 ) OR " +
      "( :type = 'negative' AND d.traded < 0 )) AND " +
      "(:startDate IS NULL OR d.createDate >= :startDate) AND " +
      "(:endDate IS NULL OR d.createDate <= :endDate) " +
      "ORDER BY d.createDate DESC")
  Page<TransactionRes> findAllByFilter(
      @Param("type") String type,
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate,
      Pageable pageable);

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.ITransaction$TransactionRes(" +
      "d.id, d.traded, d.balance, d.description, d.createDate, d.updateDate, d.status, " +
      "d.user.email) " +
      "FROM Transaction d " +
      "WHERE (:type IS NULL OR :type = '' OR " +
      "( :type = 'positive' AND d.traded > 0 ) OR " +
      "( :type = 'negative' AND d.traded < 0 )) AND " +
      "(:startDate IS NULL OR d.createDate >= :startDate) AND " +
      "(:endDate IS NULL OR d.createDate <= :endDate) " +
      "ORDER BY d.createDate DESC")
  List<TransactionRes> exportExcel(
      @Param("type") String type,
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate);

}
