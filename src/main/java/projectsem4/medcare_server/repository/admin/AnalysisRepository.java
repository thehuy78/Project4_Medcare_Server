package projectsem4.medcare_server.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import projectsem4.medcare_server.domain.entity.Booking;
import projectsem4.medcare_server.interfaces.admin.IAnalysis.ChartDataService;
import projectsem4.medcare_server.interfaces.admin.IAnalysis.ChartMapVN;

@Repository
public interface AnalysisRepository extends JpaRepository<Booking, Long> {

        @Query("SELECT " +
                        "CASE :times " +
                        "WHEN 1 THEN HOUR(b.createDate) " + // Group by hour for daily
                        "WHEN 7 THEN FUNCTION('DAYOFWEEK', b.createDate)" + // Group by day for weekly
                        "WHEN 30 THEN FUNCTION('DAYOFWEEK', b.createDate) " + // Group by weekday for monthly
                        "WHEN 365 THEN MONTH(b.createDate) " + // Group by month for yearly
                        "END AS period, " +
                        "COUNT(b) AS bookingsCount, " +
                        "SUM(CASE WHEN :type = 'revenue' THEN CAST(b.revenue AS LONG) ELSE 0 END) AS totalRevenue, " +
                        "SUM(CASE WHEN :type = 'profit' THEN CAST(b.profit AS LONG) ELSE 0 END) AS totalProfit " +
                        "FROM Booking b " +
                        "WHERE b.createDate BETWEEN :startDate AND :endDate " +
                        "AND (:hospitalCode IS NULL OR :hospitalCode = '' OR b.hospital.code = :hospitalCode) " +
                        "AND (:service IS NULL OR :service = '' OR b.type.name = :service) " +
                        "GROUP BY period " +
                        "ORDER BY period ASC")
        List<Object[]> fetchBookingData(
                        @Param("hospitalCode") String hospitalCode,
                        @Param("type") String type,
                        @Param("service") String service,
                        @Param("times") Integer times,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IAnalysis$ChartDataService(" +
                        "b.type.name AS services, " +
                        "COUNT(b) AS bookingsCount, " +
                        "SUM(CASE WHEN :type = 'revenue' THEN CAST(b.revenue AS LONG) ELSE 0 END) AS totalRevenue, " +
                        "SUM(CASE WHEN :type = 'profit' THEN CAST(b.profit AS LONG) ELSE 0 END) AS totalProfit) " +
                        "FROM Booking b " +
                        "WHERE b.createDate BETWEEN :startDate AND :endDate " +
                        "AND (:hospitalCode IS NULL OR :hospitalCode = '' OR b.hospital.code = :hospitalCode) " +
                        "GROUP BY services " +
                        "ORDER BY services ASC")
        List<ChartDataService> fetchBookingHospital(
                        @Param("hospitalCode") String hospitalCode,
                        @Param("type") String type,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IAnalysis$ChartMapVN(" +
                        "b.brief.province, " +
                        "COUNT(b) )" +
                        "FROM Booking b " +
                        "WHERE b.createDate BETWEEN :startDate AND :endDate " +
                        "AND (:vaccineCode IS NULL OR :vaccineCode = '' OR b.vaccine.code = :vaccineCode) " +
                        "GROUP BY b.brief.province")
        List<ChartMapVN> countChartMap(
                        @Param("vaccineCode") String vaccineCode,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IAnalysis$ChartMapVN(" +
                        "b.brief.province, " +
                        "COUNT(b) )" +
                        "FROM Booking b " +
                        "WHERE b.createDate BETWEEN :startDate AND :endDate " +
                        "AND (:vaccineIds IS NULL OR b.vaccine.id IN :vaccineIds) " +
                        "GROUP BY b.brief.province")
        List<ChartMapVN> countChartMapEvent(
                        @Param("vaccineIds") List<Long> vaccineIds,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

}
