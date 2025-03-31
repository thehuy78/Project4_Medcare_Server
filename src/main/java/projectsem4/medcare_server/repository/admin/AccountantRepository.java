package projectsem4.medcare_server.repository.admin;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.Booking;
import projectsem4.medcare_server.interfaces.admin.IBooking.BookingRes;

@Repository
public interface AccountantRepository extends JpaRepository<Booking, Long> {
  @Query("SELECT b FROM Booking b WHERE b.createDate >= :startDate AND b.createDate <= :endDate")
  List<Booking> findBookingsInWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IBooking$BookingRes(" +
      "d.id, d.bookingDate, d.bookingTime, d.description, d.revenue, d.profit, d.createDate, d.updateDate, d.status,"
      +
      "d.hospital.name, d.type.name, d.user.email, d.brief.name, d.brief.phone, d.brief.gender, d.brief.dob, d.brief.province, d.brief.identifier) "
      +
      "FROM Booking d " +
      "WHERE" +
      "(:hospitalCode IS NULL OR :hospitalCode = '' OR LOWER(d.hospital.code) LIKE LOWER(:hospitalCode)) AND "
      +

      "(:startDate IS NULL OR d.createDate >= :startDate) AND " +
      "(:endDate IS NULL OR d.createDate <= :endDate)"
      +
      "ORDER BY d.createDate DESC")
  List<BookingRes> findByFilters(
      @Param("hospitalCode") String hospitalCode,
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate);
}
