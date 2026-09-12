package lk.ijse.MainCabService.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register", "/api/v1/auth/customer-register", "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/vehicles/**", "/v1/vehicles").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/terms-condition", "/v1/privacy-policy", "/v1/faqs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/customer_reviews/approved").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(HttpMethod.PUT, "/api/v1/auth/update-profile/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/auth/change-email", "/api/v1/auth/change-password").authenticated()

                        .requestMatchers(HttpMethod.POST, "/v1/bookingCustomers").authenticated()
                        .requestMatchers(HttpMethod.POST, "/v1/bookings").authenticated()
                        .requestMatchers(HttpMethod.POST, "/v1/customer_reviews").authenticated()

                        .requestMatchers(HttpMethod.GET, "/v1/bookings", "/v1/bookings/search").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PATCH, "/v1/bookings/*/status").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/v1/customer_reviews").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PATCH, "/v1/customer_reviews/*/status").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/auth/user-status/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/all", "/api/v1/auth/customers").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/v1/customers/**", "/v1/customers").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/v1/vehicles").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/v1/vehicles").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/v1/rentals/**", "/v1/rentals").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/v1/returns/**", "/v1/returns").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/v1/payments/**", "/v1/payments").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/v1/maintenance/**", "/v1/maintenance").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/v1/website-settings/**", "/v1/website-settings").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/v1/terms-condition", "/v1/privacy-policy", "/v1/faqs").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/v1/terms-condition/**", "/v1/privacy-policy/**", "/v1/faqs/**").hasAnyRole("ADMIN", "STAFF")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}