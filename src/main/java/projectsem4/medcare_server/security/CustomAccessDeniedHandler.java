package projectsem4.medcare_server.security;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import projectsem4.medcare_server.domain.dto.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException {
    // Tạo một đối tượng CustomResult với thông báo từ AccessDeniedException
    CustomResult errorResult = new CustomResult(
        HttpServletResponse.SC_FORBIDDEN, // Mã lỗi 403 - Forbidden
        accessDeniedException.getMessage(), // Thông báo chi tiết của lỗi
        null);

    // Thiết lập kiểu dữ liệu phản hồi là JSON và trạng thái HTTP
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    // Ghi đối tượng CustomResult dưới dạng JSON vào phản hồi HTTP
    response.getWriter().write(convertObjectToJson(errorResult));
  }

  // Phương thức chuyển đổi đối tượng Java thành chuỗi JSON
  private String convertObjectToJson(Object object) throws IOException {
    if (object == null) {
      return null;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(object);
  }
}
