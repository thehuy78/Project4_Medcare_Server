package projectsem4.medcare_server.interfaces.customer;

import projectsem4.medcare_server.domain.dto.CustomResult;

public interface ICustomerNotification {
    CustomResult getSeenByUser(String userId);

    CustomResult getLazy(String userId, String pageNo, String typeValue);

    CustomResult deleteNotification(String notificationId);

    CustomResult seenNotification(String notificationId);
}
