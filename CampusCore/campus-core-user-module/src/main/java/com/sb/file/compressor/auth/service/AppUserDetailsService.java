package com.sb.file.compressor.auth.service;

import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */

@Service
@Transactional
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO:: check disable or not
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return userOptional.get();
    }
}
