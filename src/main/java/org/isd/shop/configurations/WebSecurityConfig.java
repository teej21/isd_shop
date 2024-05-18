package org.isd.shop.configurations;

import lombok.RequiredArgsConstructor;
import org.isd.shop.filters.JwtTokenFilter;
import org.isd.shop.responses.common.ErrorResultResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(c -> c.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(request -> {
                request.
                    requestMatchers("/register").permitAll()
                    .requestMatchers("/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/products/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                    .requestMatchers("/categories/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                    .requestMatchers("/orders/admin/**").hasAnyRole("ADMIN", "MANAGER")
                    .requestMatchers("/orders/employees/**").hasAnyRole("EMPLOYEE", "ADMIN", "MANAGER")
                    .requestMatchers("/orders/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE", "CUSTOMER")
                    .requestMatchers("/order-details/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE", "CUSTOMER")
                    .anyRequest().authenticated()
                ;
            })
            .exceptionHandling(e ->
                e.accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(403);
                    request.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json");
                    ErrorResultResponse errorResultResponse = new ErrorResultResponse("Bạn Không Có Quyền Truy Cập Vào Tài Nguyên Này");
                    response.getWriter().write(errorResultResponse.toString());


                })
            );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
