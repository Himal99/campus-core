package com.sb.file.compressor.auth.provider;

import com.sb.file.compressor.core.enums.Status;
import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.entity.Role;
import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.enums.Roles;
import com.sb.file.compressor.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class AuthenticationProviderForApiUsers implements AuthenticationProvider {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String WRONG_CREDENTIALS = "Email or password is wrong";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<User> userDetails = userRepository.findByEmail(email);
        if (userDetails.isEmpty()) {
            throw new CustomException(WRONG_CREDENTIALS);
        } else {
            if (!passwordEncoder.matches(password, userDetails.get().getEncodedPassword())) {
                throw new BadCredentialsException(WRONG_CREDENTIALS);
            } else if (userDetails.get().getStatus().equals(Status.INACTIVE)){
                throw new BadCredentialsException("User is inactive");
            }
        }
        return new UsernamePasswordAuthenticationToken(email, password, userDetails.get().getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}

