// package projectsem4.medcare_server.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class CorsConfig {

// @Bean
// public WebMvcConfigurer corsConfigurer() {
// return new WebMvcConfigurer() {
// @Override
// public void addCorsMappings(CorsRegistry registry) {
// registry.addMapping("/**") // Áp dụng cho tất cả các endpoint
// .allowedOrigins("http://localhost:3000", "http://localhost:3001",
// "http://localhost:3002") // Nguồn gốc được
// .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức
// HTTP được phép
// .allowedHeaders("*")
// .exposedHeaders("Authorization")
// // Các header được phép
// .allowCredentials(true); // Cho phép gửi cookie cùng request
// }
// };
// }
// }
