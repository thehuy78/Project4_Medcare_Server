package projectsem4.medcare_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import projectsem4.medcare_server.security.CustomAccessDeniedHandler;
import projectsem4.medcare_server.security.CustomAuthenticationEntryPoint;
import projectsem4.medcare_server.security.JwtRequestFilter;

@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true) // Kích hoạt hỗ trợ @RolesAllowed
public class SecurityConfig {

        @Autowired
        private JwtRequestFilter jwtRequestFilter;

        // @Autowired
        // private CorsConfig corsConfig;

        @Autowired
        private CustomAuthenticationEntryPoint authenticationEntryPoint;

        @Autowired
        private CustomAccessDeniedHandler accessDeniedHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // .cors(cors -> corsConfig.corsConfigurer())
                                .csrf(csrf -> csrf.disable())
                                // Disable CSRF protection
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/api/customer/auth/register",
                                                                "/api/customer/auth/resetPassword",
                                                                "/api/customer/auth/resendverificationcode/**",
                                                                "/api/customer/auth/verificationEmail",
                                                                "/api/customer/auth/forgotPasswordRequestCustomer/*",
                                                                "/api/customer/auth/login")
                                                .permitAll()
                                                .requestMatchers("/api/customer/hospital/**").permitAll()
                                                .requestMatchers("/api/customer/department/**").permitAll()
                                                .requestMatchers("/api/customer/doctor/**").permitAll()
                                                .requestMatchers("/api/customer/package/**").permitAll()
                                                .requestMatchers("/api/admin/auth/forgot",
                                                                "/api/admin/auth/resetpassword",
                                                                "/api/admin/auth/login",
                                                                "/api/admin/auth/loginqr",
                                                                "/api/admin/auth/requestqr/**",
                                                                "/api/admin/auth/getuserqr/**",
                                                                "/api/admin/auth/loginadmin")
                                                .permitAll()
                                                .anyRequest().authenticated() // Require authentication for all other
                                // requests
                                )
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(authenticationEntryPoint) // Sử dụng
                                                                                                    // CustomAuthenticationEntryPoint
                                                .accessDeniedHandler(accessDeniedHandler) // Sử dụng
                                                                                          // CustomAccessDeniedHandler
                                )
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless
                                                                                                        // sessions
                                );

                // Add JWT filter before the UsernamePasswordAuthenticationFilter
                http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
