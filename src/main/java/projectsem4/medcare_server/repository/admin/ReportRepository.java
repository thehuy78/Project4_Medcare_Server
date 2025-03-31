package projectsem4.medcare_server.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.Booking;
import projectsem4.medcare_server.interfaces.admin.IBooking.BookingRes;
import projectsem4.medcare_server.interfaces.admin.IDoctor.DoctorTopRes;

import java.util.*;

@Repository
public interface ReportRepository extends JpaRepository<Booking, Long> {
  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IBooking$BookingRes(" +
      "d.id, d.bookingDate, d.bookingTime, d.description, d.revenue, d.profit, d.createDate, d.updateDate, d.status,"
      +
      "d.hospital.name, d.type.name, d.user.email, d.brief.name, d.brief.phone, d.brief.gender, d.brief.dob, d.brief.province, d.brief.identifier) "
      +
      "FROM Booking d " +
      "WHERE (:gender IS NULL OR :gender = '' OR LOWER(d.brief.gender) LIKE LOWER(:gender)) AND " +
      "(:type IS NULL OR :type = '' OR LOWER(d.type.name) LIKE LOWER(:type)) AND " +
      "(:hospitalCode IS NULL OR :hospitalCode = '' OR LOWER(d.hospital.code) LIKE LOWER(:hospitalCode)) AND "
      +
      "(:revenueStart IS NULL OR :revenueEnd IS NULL OR (d.revenue > :revenueStart AND d.revenue <= :revenueEnd)) AND "
      +
      "(:startDate IS NULL OR d.createDate >= :startDate) AND " +
      "(:endDate IS NULL OR d.createDate <= :endDate) AND" +
      "(:search IS NULL OR :search = '' OR LOWER(d.user.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
      +
      "ORDER BY d.createDate DESC")
  List<BookingRes> findByFilters(
      @Param("search") String search,
      @Param("gender") String gender,
      @Param("type") String type,
      @Param("hospitalCode") String hospitalCode,
      @Param("revenueStart") Double revenueStart,
      @Param("revenueEnd") Double revenueEnd,
      @Param("startDate") Date startDate, // New parameter for start date
      @Param("endDate") Date endDate);

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IBooking$BookingRes(" +
      "d.id, d.bookingDate, d.bookingTime, d.description, d.revenue, d.profit, d.createDate, d.updateDate, d.status,"
      +
      "d.hospital.name, d.type.name, d.user.email, d.brief.name, d.brief.phone, d.brief.gender, d.brief.dob, d.brief.province, d.brief.identifier) "
      +
      "FROM Booking d " +
      "WHERE (:gender IS NULL OR :gender = '' OR LOWER(d.brief.gender) LIKE LOWER(:gender)) AND " +
      "(:type IS NULL OR :type = '' OR LOWER(d.type.name) LIKE LOWER(:type)) AND " +
      "(:typeUrl IS NULL OR :typeUrl = '' OR LOWER(d.type.name) LIKE LOWER(:typeUrl)) AND " +
      "(:idUrl IS NULL OR :idUrl = '' OR LOWER(d.doctor.code) LIKE LOWER(:idUrl)) AND " +
      "(:revenueStart IS NULL OR :revenueEnd IS NULL OR (d.revenue > :revenueStart AND d.revenue <= :revenueEnd)) AND "
      +
      "(:startDate IS NULL OR d.createDate >= :startDate) AND " +
      "(:endDate IS NULL OR d.createDate <= :endDate) AND" +
      "(:search IS NULL OR :search = '' OR LOWER(d.user.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
      +
      "ORDER BY d.createDate DESC")
  List<BookingRes> findByFiltersDoctor(
      @Param("search") String search,
      @Param("gender") String gender,
      @Param("type") String type,
      @Param("typeUrl") String typeUrl,
      @Param("idUrl") String idUrl,
      @Param("revenueStart") Double revenueStart,
      @Param("revenueEnd") Double revenueEnd,
      @Param("startDate") Date startDate, // New parameter for start date
      @Param("endDate") Date endDate);

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IBooking$BookingRes(" +
      "d.id, d.bookingDate, d.bookingTime, d.description, d.revenue, d.profit, d.createDate, d.updateDate, d.status,"
      +
      "d.hospital.name, d.type.name, d.user.email, d.brief.name, d.brief.phone, d.brief.gender, d.brief.dob, d.brief.province, d.brief.identifier) "
      +
      "FROM Booking d " +
      "WHERE (:gender IS NULL OR :gender = '' OR LOWER(d.brief.gender) LIKE LOWER(:gender)) AND " +
      "(:type IS NULL OR :type = '' OR LOWER(d.type.name) LIKE LOWER(:type)) AND " +
      "(:typeUrl IS NULL OR :typeUrl = '' OR LOWER(d.type.name) LIKE LOWER(:typeUrl)) AND " +
      "(:idUrl IS NULL OR :idUrl = '' OR LOWER(d.pack.code) LIKE LOWER(:idUrl)) AND " +
      "(:revenueStart IS NULL OR :revenueEnd IS NULL OR (d.revenue > :revenueStart AND d.revenue <= :revenueEnd)) AND "
      +
      "(:startDate IS NULL OR d.createDate >= :startDate) AND " +
      "(:endDate IS NULL OR d.createDate <= :endDate) AND" +
      "(:search IS NULL OR :search = '' OR LOWER(d.user.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
      +
      "ORDER BY d.createDate DESC")
  List<BookingRes> findByFiltersPack(
      @Param("search") String search,
      @Param("gender") String gender,
      @Param("type") String type,
      @Param("typeUrl") String typeUrl,
      @Param("idUrl") String idUrl,
      @Param("revenueStart") Double revenueStart,
      @Param("revenueEnd") Double revenueEnd,
      @Param("startDate") Date startDate, // New parameter for start date
      @Param("endDate") Date endDate);

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IBooking$BookingRes(" +
      "d.id, d.bookingDate, d.bookingTime, d.description, d.revenue, d.profit, d.createDate, d.updateDate, d.status,"
      +
      "d.hospital.name, d.type.name, d.user.email, d.brief.name, d.brief.phone, d.brief.gender, d.brief.dob, d.brief.province, d.brief.identifier) "
      +
      "FROM Booking d " +
      "WHERE (:gender IS NULL OR :gender = '' OR LOWER(d.brief.gender) LIKE LOWER(:gender)) AND " +
      "(:type IS NULL OR :type = '' OR LOWER(d.type.name) LIKE LOWER(:type)) AND " +
      "(:typeUrl IS NULL OR :typeUrl = '' OR LOWER(d.type.name) LIKE LOWER(:typeUrl)) AND " +
      "(:idUrl IS NULL OR :idUrl = '' OR LOWER(d.test.code) LIKE LOWER(:idUrl)) AND " +
      "(:revenueStart IS NULL OR :revenueEnd IS NULL OR (d.revenue > :revenueStart AND d.revenue <= :revenueEnd)) AND "
      +
      "(:startDate IS NULL OR d.createDate >= :startDate) AND " +
      "(:endDate IS NULL OR d.createDate <= :endDate) AND" +
      "(:search IS NULL OR :search = '' OR LOWER(d.user.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
      +
      "ORDER BY d.createDate DESC")
  List<BookingRes> findByFiltersTest(
      @Param("search") String search,
      @Param("gender") String gender,
      @Param("type") String type,
      @Param("typeUrl") String typeUrl,
      @Param("idUrl") String idUrl,
      @Param("revenueStart") Double revenueStart,
      @Param("revenueEnd") Double revenueEnd,
      @Param("startDate") Date startDate, // New parameter for start date
      @Param("endDate") Date endDate);

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IBooking$BookingRes(" +
      "d.id, d.bookingDate, d.bookingTime, d.description, d.revenue, d.profit, d.createDate, d.updateDate, d.status,"
      +
      "d.hospital.name, d.type.name, d.user.email, d.brief.name, d.brief.phone, d.brief.gender, d.brief.dob, d.brief.province, d.brief.identifier) "
      +
      "FROM Booking d " +
      "WHERE (:gender IS NULL OR :gender = '' OR LOWER(d.brief.gender) LIKE LOWER(:gender)) AND " +
      "(:type IS NULL OR :type = '' OR LOWER(d.type.name) LIKE LOWER(:type)) AND " +
      "(:typeUrl IS NULL OR :typeUrl = '' OR LOWER(d.type.name) LIKE LOWER(:typeUrl)) AND " +
      "(:idUrl IS NULL OR :idUrl = '' OR LOWER(d.vaccine.code) LIKE LOWER(:idUrl)) AND " +
      "(:revenueStart IS NULL OR :revenueEnd IS NULL OR (d.revenue > :revenueStart AND d.revenue <= :revenueEnd)) AND "
      +
      "(:startDate IS NULL OR d.createDate >= :startDate) AND " +
      "(:endDate IS NULL OR d.createDate <= :endDate) AND" +
      "(:search IS NULL OR :search = '' OR LOWER(d.user.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
      +
      "ORDER BY d.createDate DESC")
  List<BookingRes> findByFiltersVaccine(
      @Param("search") String search,
      @Param("gender") String gender,
      @Param("type") String type,
      @Param("typeUrl") String typeUrl,
      @Param("idUrl") String idUrl,
      @Param("revenueStart") Double revenueStart,
      @Param("revenueEnd") Double revenueEnd,
      @Param("startDate") Date startDate, // New parameter for start date
      @Param("endDate") Date endDate);

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IBooking$BookingRes(" +
      "b.id, b.bookingDate, b.bookingTime, b.description, b.revenue, b.profit, " +
      "b.createDate, b.updateDate, b.status, b.hospital.name, " +
      "b.type.name, b.user.email, b.brief.name, b.brief.phone, b.brief.gender, " +
      "b.brief.dob, b.brief.province, b.brief.identifier) " +
      "FROM Booking b " +
      "WHERE (:type IS NULL OR b.type.name = :type) " +
      "AND (:startDate IS NULL OR b.createDate >= :startDate) " +
      "AND (:endDate IS NULL OR b.createDate <= :endDate)")
  List<BookingRes> findBookingsWithFilter(
      @Param("type") String type,
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate);

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IBooking$BookingRes(" +
      "d.id, d.bookingDate, d.bookingTime, d.description, d.revenue, d.profit, d.createDate, d.updateDate, d.status,"
      +
      "d.hospital.name, d.type.name, d.user.email, d.brief.name, d.brief.phone, d.brief.gender, d.brief.dob, d.brief.province, d.brief.identifier) "
      +
      "FROM Booking d " +
      "WHERE " +
      "(:type IS NULL OR :type = '' OR LOWER(d.type.name) LIKE LOWER(:type)) AND " +
      "(:hospital IS NULL OR :hospital = '' OR LOWER(d.hospital.code) LIKE LOWER(:hospital)) AND " +
      "(:startDate IS NULL OR d.createDate >= :startDate) AND " +
      "(:endDate IS NULL OR d.createDate <= :endDate) AND" +
      "(:search IS NULL OR :search = '' OR LOWER(d.user.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
      +
      "ORDER BY d.createDate DESC")
  List<BookingRes> findByHospitalType(
      @Param("search") String search,

      @Param("type") String type,
      @Param("hospital") String hospital,

      @Param("startDate") Date startDate, // New parameter for start date
      @Param("endDate") Date endDate);

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IBooking$BookingRes(" +
      "d.id, d.bookingDate, d.bookingTime, d.description, d.revenue, d.profit, d.createDate, d.updateDate, d.status,"
      +
      "d.hospital.name, d.type.name, d.user.email, d.brief.name, d.brief.phone, d.brief.gender, d.brief.dob, d.brief.province, d.brief.identifier) "
      +
      "FROM Booking d " +
      "WHERE " +
      "(:hospital IS NULL OR :hospital = '' OR LOWER(d.hospital.code) LIKE LOWER(:hospital)) AND " +
      "(:search IS NULL OR :search = '' OR LOWER(d.user.email) LIKE LOWER(CONCAT('%', :search, '%')) "
      +
      "OR LOWER(d.brief.phone) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
      "(CAST(:search AS string) IS NOT NULL AND d.id = CAST(:search AS long)))" +
      "ORDER BY d.createDate DESC")
  List<BookingRes> searchBooking(
      @Param("search") String search,
      @Param("hospital") String hospital);

  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IDoctor$DoctorTopRes(" +
      "d.id, d.code, d.name, d.level, d.avatar, COUNT(b.id)) " +
      "FROM Booking b " +
      "JOIN b.doctor d " +
      "WHERE b.hospital.id = :hospitalId " +
      "AND b.createDate BETWEEN :startDate AND :endDate " +
      "GROUP BY d.id, d.code, d.name, d.level, d.avatar " +
      "ORDER BY COUNT(b.id) DESC")
  List<DoctorTopRes> findTopDoctorsByBookings(
      @Param("hospitalId") Long hospitalId,
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate);
}
