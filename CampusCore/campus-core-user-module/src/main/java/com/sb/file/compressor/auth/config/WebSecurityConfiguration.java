package com.sb.file.compressor.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.file.compressor.auth.config.handlers.AppAuthenticationEntryPoint;
import com.sb.file.compressor.auth.filter.SecurityFilter;
import com.sb.file.compressor.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {


    @Bean
    public AppAuthenticationEntryPoint entryPoint(){
        return new AppAuthenticationEntryPoint(new ObjectMapper());
    }

    protected static final String[] WHITE_LIST_API = new String[]{
            "/api/v1/auth/login","/api/v1/auth/login/api-users","/",
            "/api/v1/compress/callback",
            "/api/v1/compress/realtime-server-logs",
            "/api/v1/realtime/**",
            "/api/v1/compress-files",
            "/api/v1/download/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( authorize -> authorize
                        .requestMatchers(WHITE_LIST_API).permitAll()
                        .requestMatchers("/api/v1/user-info/**").hasAuthority(Roles.ADMIN.getValue())
                        .requestMatchers("/api/v1/admin/**").hasAuthority(Roles.ADMIN.getValue())
                        .requestMatchers("/api/v1/editor/**").hasAnyAuthority(
                                Roles.ADMIN.getValue(), Roles.EDITOR.getValue())
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e-> e.authenticationEntryPoint(entryPoint()))
                .sessionManagement( s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(securityFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout(this::logoutHandler);

        return http.build();
    }

    private void logoutHandler(LogoutConfigurer<HttpSecurity> logout) {
        logout.logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/auth/logout"));
        logout.logoutSuccessUrl("/api/v1/auth/login");
    }

    @Bean
    public SecurityFilter securityFilter() {
        return new SecurityFilter();
    }

    @Bean
    public FilterRegistrationBean customCorsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }

}
