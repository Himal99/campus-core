package com.sb.file.compressor.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.file.compressor.auth.config.SpringSecurityAuditorAware;
import com.sb.file.compressor.auth.service.JwtService;
import com.sb.file.compressor.repo.RoleRepository;
import com.sb.file.compressor.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = "com.sb.file.compressor.*")
@ComponentScan(basePackages = "com.sb.file.compressor")
@EnableJpaRepositories(basePackages = "com.sb.file.compressor")
@EntityScan(basePackages = "com.sb.file.compressor")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SbBackCoreApplication extends SpringBootServletInitializer {
    @Autowired
    DataSource dataSource;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    public static void main(String[] args) {
        SpringApplication.run(SbBackCoreApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtService jwtService(){
        return new JwtService();
    }

    @PostConstruct
    public void init() {

        if (userRepository.findAll().isEmpty() || roleRepository.findAll().size()<=1) {
            ClassPathResource resource = new ClassPathResource(
                    "/db/user.sql");
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(resource);
            populator.execute(dataSource);
        }
    }

    @Bean
    public AuditorAware<Long> auditorAware() {
        return new SpringSecurityAuditorAware(new NamedParameterJdbcTemplate(dataSource));
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
    }

}
