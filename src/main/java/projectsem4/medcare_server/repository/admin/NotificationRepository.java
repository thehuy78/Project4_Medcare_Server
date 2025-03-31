package projectsem4.medcare_server.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import projectsem4.medcare_server.domain.entity.Notification;

import projectsem4.medcare_server.interfaces.admin.INotification.NotificationRes;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.INotification$NotificationRes(" +
                        "n.id, n.type, n.description, n.createDate,n.status) " +
                        "FROM Notification n  WHERE n.createDate BETWEEN :startDate AND :endDate AND" +
                        "(:idUser IS NULL OR :idUser = n.user.id) AND" +
                        "(:type IS NULL OR :type = '' OR LOWER(n.type) LIKE LOWER(CONCAT('%', :type, '%'))) AND " +
                        "(n.isDeleted = 'undeleted')" +
                        "ORDER BY n.createDate DESC")
        Page<NotificationRes> findByFilters(
                        @Param("idUser") Long idUser,
                        @Param("type") String type,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate,
                        Pageable pageable);

        @Modifying
        @Query("UPDATE Notification n SET n.isDeleted = 'deleted' WHERE n.user.id = :userId")
        void updateIsDeletedByUserId(@Param("userId") Long userId);
}
