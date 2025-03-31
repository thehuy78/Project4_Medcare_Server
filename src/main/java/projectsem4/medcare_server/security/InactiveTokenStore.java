package projectsem4.medcare_server.security;

import org.springframework.stereotype.Component;
import java.util.concurrent.*;

@Component
public class InactiveTokenStore {
  // Lưu trữ token và thời gian hết hạn tương ứng
  private final ConcurrentHashMap<String, Long> inactiveTokens = new ConcurrentHashMap<>();
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  public InactiveTokenStore() {
    // Khởi động một task để xóa token đã hết hạn mỗi phút
    scheduler.scheduleAtFixedRate(this::cleanupExpiredTokens, 1, 1, TimeUnit.MINUTES);
  }

  public ConcurrentHashMap<String, Long> addToken(String token, long expirationTime) {
    // Thêm token và thời gian hết hạn
    inactiveTokens.put(token, expirationTime);
    return inactiveTokens;
  }

  public boolean isTokenInactive(String token) {
    return inactiveTokens.containsKey(token);
  }

  private void cleanupExpiredTokens() {
    long currentTime = System.currentTimeMillis();
    inactiveTokens.entrySet().removeIf(entry -> entry.getValue() < currentTime);
  }
}
