package com.sb.file.compressor.auth.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Himal Rai on 2/21/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

public class SpringSecurityAuditorAware implements AuditorAware<Long> {


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SpringSecurityAuditorAware(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        if (authentication.getName() != null) {
            Long id = getCurrentUserId(authentication.getName());
            return Optional.of(id);
        }

        throw new RuntimeException("Invalid token");
    }


    private Long getCurrentUserId(String email) {
        Map<String, Object> map = new HashMap<>();
        String query = "SELECT id from users where email = :email";
        map.put("email", email);
        Long id = namedParameterJdbcTemplate.queryForObject(query, map, Long.class);
        return id;
    }

}
