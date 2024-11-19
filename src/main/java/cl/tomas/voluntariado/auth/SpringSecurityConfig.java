package cl.tomas.voluntariado.auth;

import cl.tomas.voluntariado.auth.filter.JwtAuthenticationFilter;
import cl.tomas.voluntariado.auth.filter.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("USER", "ADMIN", "ORGANIZADOR")
                                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/contact").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/contact").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/contact/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/contact/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/contact/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("USER", "ADMIN", "ORGANIZADOR")
                                .requestMatchers(HttpMethod.GET, "/api/organization").hasAnyRole("USER", "ADMIN", "ORGANIZADOR")
                                .requestMatchers(HttpMethod.POST, "/api/organization").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/organization/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/organization/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/organization/{id}").hasAnyRole("USER", "ADMIN", "ORGANIZADOR")
                                .anyRequest().authenticated()
                )
    .addFilter(new JwtAuthenticationFilter(authenticationManager()))
    .addFilter(new JwtValidationFilter(authenticationManager()))
    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
    .csrf(AbstractHttpConfigurer::disable)
    .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(this.corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}

