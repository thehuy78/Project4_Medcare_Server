package projectsem4.medcare_server.repository.customer;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.entity.Notification;
import projectsem4.medcare_server.domain.entity.User;

public interface CustomerNotificationRepo extends JpaRepository<Notification, Long> {

    @Query(" SELECT new projectsem4.medcare_server.repository.customer.CustomerNotificationRepo$NotificationDTO(n.id,n.description , n.status , n.type , n.createDate  ) FROM Notification n WHERE n.isDeleted = 'undeleted' AND n.status = 'unread' AND n.user =:user ")
    List<NotificationDTO> getAllByUser(@Param("user") User user);

    @Query(" SELECT new projectsem4.medcare_server.repository.customer.CustomerNotificationRepo$NotificationDTO(n.id,n.description , n.status , n.type , n.createDate  ) FROM Notification n WHERE n.isDeleted = 'undeleted' AND n.user =:user AND (:typeValue IS NULL OR n.type =:typeValue) "
            +
            " ORDER BY " +
            "CASE WHEN n.status = 'active' THEN 0 ELSE 1 END, " +
            " n.createDate DESC  ")
    Page<NotificationDTO> getLazy(@Param("user") User user, Pageable pageable, @Param("typeValue") String typeValue);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class NotificationDTO {
        Long id;
        String description;
        String status;
        String type;
        Date createAt;
    }
}
