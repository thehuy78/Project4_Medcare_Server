package projectsem4.medcare_server.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Service;

@Service
public class FirebaseRealtimeDatabaseService {

  public void sendRealtimeMessageToUser(String userId, String message) {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("notifications/" + userId);

    // Sử dụng ApiFuture để xử lý kết quả của setValueAsync
    ApiFuture<Void> future = ref.setValueAsync(message);

    // Xử lý kết quả không đồng bộ
    future.addListener(() -> {
      try {
        future.get(); // Chờ kết quả từ tác vụ không đồng bộ
        System.out.println("Notification sent successfully to user: " + userId);
      } catch (Exception e) {
        System.err.println("Error sending notification: " + e.getMessage());
      }
    }, Runnable::run);
  }
}
