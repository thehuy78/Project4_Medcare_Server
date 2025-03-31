package projectsem4.medcare_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import projectsem4.medcare_server.domain.dto.CustomResult;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Xử lý lỗi truy cập không được phép (403 - Forbidden)
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<CustomResult> handleAccessDeniedException(HttpServletRequest request,
      AccessDeniedException ex) {
    CustomResult errorResult = new CustomResult(
        HttpStatus.FORBIDDEN.value(),
        "Access Denied: You do not have permission to access this resource.",
        null);
    return new ResponseEntity<>(errorResult, HttpStatus.FORBIDDEN);
  }

  // Xử lý các lỗi xác thực (401 - Unauthorized)
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<CustomResult> handleAuthenticationException(HttpServletRequest request,
      AuthenticationException ex) {
    CustomResult errorResult = new CustomResult(
        HttpStatus.UNAUTHORIZED.value(),
        "Authentication Failed: Please log in to access this resource.",
        null);
    return new ResponseEntity<>(errorResult, HttpStatus.UNAUTHORIZED);
  }

  // Xử lý các lỗi chung khác (500 - Internal Server Error)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<CustomResult> handleGeneralException(HttpServletRequest request, Exception ex) {
    CustomResult errorResult = new CustomResult(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "An unexpected error occurred: " + ex.getMessage(),
        null);
    return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
