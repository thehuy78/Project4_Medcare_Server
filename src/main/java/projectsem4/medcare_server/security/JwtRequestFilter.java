package projectsem4.medcare_server.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import projectsem4.medcare_server.domain.entity.*;
import projectsem4.medcare_server.service.admin.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserService userService;

  @Autowired
  private InactiveTokenStore inactiveTokenStore;

  // @Override
  // protected boolean shouldNotFilter(HttpServletRequest request) throws
  // ServletException {
  // // Các route không cần filter
  // String path = request.getRequestURI();
  // return path.equals("/api/admin/auth/login") ||
  // path.equals("/api/admin/auth/register");
  // }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    final String authorizationHeader = request.getHeader("Authorization");

    // final String authorizationHeader = "Bearer
    // eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJodXkiLCJpZCI6MSwiZmlyc3RuYW1lIjoiVGhlIEh1eSIsImxhc3RuYW1lIjoiTmd1eWVuIiwicm9sZSI6ImN1c3RvbWVyIiwiaWF0IjoxNzI5NTY1MTAzLCJleHAiOjE3Mjk2MDExMDN9.DEQCqdaRn7ermmu9l2z7rDIVsowxakjmJnln2N5dvSw";

    String email = null;
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);

      if (inactiveTokenStore.isTokenInactive(jwt)) {
        // Nếu token nằm trong danh sách không hợp lệ, từ chối yêu cầu
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid");
        return;
      }

      try {
        email = jwtUtil.extractEmail(jwt);
      } catch (ExpiredJwtException e) {
        System.out.println("JWT Token has expired");
      } catch (Exception e) {
        System.out.println("Error parsing JWT token");
      }
    }

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      if (jwtUtil.validateToken(jwt, email)) {
        User user = userService.findByEmail(email);

        if (user != null) {
          List<GrantedAuthority> authorities = Collections
              .singletonList(new SimpleGrantedAuthority("ROLE_" + user.getUserDetail().getRole()));

          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
              user, null, authorities);

          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

      }
    }

    chain.doFilter(request, response);
  }
}
