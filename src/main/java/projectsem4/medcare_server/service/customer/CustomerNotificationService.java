package projectsem4.medcare_server.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Notification;
import projectsem4.medcare_server.domain.entity.User;
import projectsem4.medcare_server.interfaces.customer.ICustomerNotification;
import projectsem4.medcare_server.repository.customer.CustomerAuthRepo;
import projectsem4.medcare_server.repository.customer.CustomerNotificationRepo;
import projectsem4.medcare_server.service.FirebaseRealtimeDatabaseService;

@Service
public class CustomerNotificationService implements ICustomerNotification {

    @Autowired
    CustomerNotificationRepo _CustomerNotificationRepo;

    @Autowired
    CustomerAuthRepo _CustomerAuthRepo;

    @Autowired
    FirebaseRealtimeDatabaseService _FirebaseRealtimeDatabaseService;

    @Override
    public CustomResult getSeenByUser(String userId) {
        try {
            User u = _CustomerAuthRepo.findById(Long.parseLong(userId)).get();
            return new CustomResult(200, "Success",
                    _CustomerNotificationRepo.getAllByUser(u));
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult deleteNotification(String notificationId) {
        try {
            Notification notification = _CustomerNotificationRepo.findById(Long.parseLong(notificationId)).get();
            notification.setIsDeleted("deleted");
            _CustomerNotificationRepo.save(notification);
            return new CustomResult(200, "Success", null);
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult seenNotification(String notificationId) {
        try {
            Notification notification = _CustomerNotificationRepo.findById(Long.parseLong(notificationId)).get();
            User u = notification.getUser();
            notification.setStatus("read");
            _CustomerNotificationRepo.save(notification);
            Long timestamp = System.currentTimeMillis();
            _FirebaseRealtimeDatabaseService.sendRealtimeMessageToUser(
                    "notificationCustomer/" + u.getId().toString(),
                    timestamp.toString());
            return new CustomResult(200, "Success", null);
        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

    @Override
    public CustomResult getLazy(String userId, String pageNo, String typeValue) {
        try {
            User u = _CustomerAuthRepo.findById(Long.parseLong(userId)).get();
            Pageable pageable = PageRequest.of(Integer.parseInt(pageNo), 10);
            if (typeValue.equals("All")) {
                return new CustomResult(200, "Success",
                        _CustomerNotificationRepo.getLazy(u, pageable, null));
            }
            return new CustomResult(200, "Success",
                    _CustomerNotificationRepo.getLazy(u, pageable, typeValue));

        } catch (Exception e) {
            return new CustomResult(400, "Server Error", null);
        }
    }

}
