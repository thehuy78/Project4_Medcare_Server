package projectsem4.medcare_server.repository.admin;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projectsem4.medcare_server.domain.entity.Booking;
import projectsem4.medcare_server.interfaces.admin.IChart.AgeGroupCount;
import projectsem4.medcare_server.interfaces.admin.IChart.BookingByTypeRes;
import projectsem4.medcare_server.interfaces.admin.IChart.TopHospitalRespon;
import projectsem4.medcare_server.interfaces.admin.IChart.TotalBookingByHoursRes;
import projectsem4.medcare_server.interfaces.admin.IChart.TotalGenderRes;
import projectsem4.medcare_server.interfaces.admin.IChart.TotalRevenueRes;

@Repository
public interface ChartRepository extends JpaRepository<Booking, Long> {
        // chart tính doanh thu lợi nhuận
        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IChart$TotalRevenueRes(COALESCE(SUM(r.revenue), 0),COALESCE(SUM(r.profit), 0)) "
                        +
                        "FROM Booking r " +
                        "WHERE r.createDate BETWEEN :startDay AND :endDay")
        TotalRevenueRes TotalRevenueByDay(
                        @Param("startDay") Date startDay,
                        @Param("endDay") Date endDay);

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IChart$TotalRevenueRes(COALESCE(SUM(r.revenue), 0),0.0) "
                        +
                        "FROM Booking r WHERE" +
                        "(:code IS NULL OR :code = '' OR LOWER(r.hospital.code) LIKE LOWER(:code)) AND " +
                        "r.createDate BETWEEN :startDay AND :endDay ")
        TotalRevenueRes TotalRevenueByDayHospital(
                        @Param("startDay") Date startDay,
                        @Param("endDay") Date endDay,
                        @Param("code") String code);

        // chart giới tính theo booking
        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IChart$TotalGenderRes("
                        + "COALESCE(SUM(CASE WHEN LOWER(r.brief.gender) = 'male' THEN 1 ELSE 0 END), 0), "
                        + "COALESCE(SUM(CASE WHEN LOWER(r.brief.gender) = 'female' THEN 1 ELSE 0 END), 0)) "
                        + "FROM Booking r "
                        + "WHERE r.createDate BETWEEN :startDay AND :endDay")
        TotalGenderRes TotalGenderByDay(
                        @Param("startDay") Date startDay,
                        @Param("endDay") Date endDay);

        // đếm booking theo giờ
        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IChart$TotalBookingByHoursRes("
                        + "HOUR(r.createDate), COUNT(r)) "
                        + "FROM Booking r "
                        + "WHERE DATE(r.createDate) = DATE(:specificDate) "
                        + "GROUP BY HOUR(r.createDate) "
                        + "ORDER BY HOUR(r.createDate)")
        List<TotalBookingByHoursRes> countBookingsByHour(@Param("specificDate") Date specificDate);

        // count theo age user booking
        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IChart$AgeGroupCount(" +
                        "COALESCE(SUM(CASE WHEN FUNCTION('DATEDIFF', CURRENT_DATE, r.brief.dob) < 365*18 THEN 1 ELSE 0 END), 0), "
                        +
                        "COALESCE(SUM(CASE WHEN FUNCTION('DATEDIFF', CURRENT_DATE, r.brief.dob) >= 365*18 AND FUNCTION('DATEDIFF', CURRENT_DATE, r.brief.dob) <= 365*50 THEN 1 ELSE 0 END), 0), "
                        +
                        "COALESCE(SUM(CASE WHEN FUNCTION('DATEDIFF', CURRENT_DATE, r.brief.dob) > 365*50 THEN 1 ELSE 0 END), 0)) "
                        +
                        "FROM Booking r " +
                        "WHERE r.createDate BETWEEN :startDay AND :endDay")
        AgeGroupCount countBookingsByAgeGroup(
                        @Param("startDay") Date startDay,
                        @Param("endDay") Date endDay);

        // count by day off week
        @Query("SELECT COUNT(b) FROM Booking b WHERE DATE(b.createDate) = DATE(:date)")
        Long countBookingsByDate(@Param("date") Date date);

        // count orderby hospital theo booking
        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IChart$TopHospitalRespon(" +
                        "b.hospital.id, b.hospital.code, " +
                        "COUNT(b) AS bookingCount, " +
                        "COALESCE(SUM(b.revenue), 0) AS revenueTotal, " +
                        "COALESCE(SUM(b.profit), 0) AS profitTotal) " +
                        "FROM Booking b " +
                        "WHERE b.createDate BETWEEN :startDate AND :endDate " +
                        "GROUP BY b.hospital.id, b.hospital.code " +
                        "ORDER BY bookingCount DESC")
        List<TopHospitalRespon> countBookingsByTopHospital(@Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        // tong so booking
        @Query("SELECT COUNT(b.id) " +
                        "FROM Booking b " +
                        "WHERE b.createDate BETWEEN :startDate AND :endDate")
        Long countTotalBookings(@Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        // tong so booking groupby Type Booking
        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IChart$BookingByTypeRes(" +
                        "b.type.id, b.type.name, " +
                        "COUNT(b) AS count) " +
                        "FROM Booking b " +
                        "WHERE b.createDate BETWEEN :startDate AND :endDate " +
                        "GROUP BY b.type.id, b.type.name " +
                        "ORDER BY count DESC")
        List<BookingByTypeRes> countBookingGroupByType(@Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

}
