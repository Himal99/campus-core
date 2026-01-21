package com.sb.file.compressor.auth.filter;

import com.sb.file.compressor.auth.provider.AppAuthenticationProvider;
import com.sb.file.compressor.auth.service.AppUserDetailsService;
import com.sb.file.compressor.auth.service.JwtService;
import com.sb.file.compressor.controller.UserControllerApis;
import com.sb.file.compressor.core.enums.Status;
import com.sb.file.compressor.entity.Role;
import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.enums.Roles;
import com.sb.file.compressor.service.contract.ApiUserTokenService;
import com.sb.file.compressor.service.contract.UserService;
import com.sb.file.compressor.utils.AppConstants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;


/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppUserDetailsService userDetailsService;

    @Autowired
    private AppAuthenticationProvider authenticationProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private ApiUserTokenService apiUserTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {


            String bearerToken = request.getHeader("Authorization");
            String username = null;
            String token = null;
            // Check if the requested URL is exempted
            if (shouldNotFilter(request)) {
                // Call the next filter in the chain without performing any filtering
                filterChain.doFilter(request, response);
                return;
            }

            if (bearerToken != null && bearerToken.startsWith("Bearer")) {
                token = bearerToken.substring(7);
                username = this.jwtService.extractUsername(token);
            } else {
                throw new BadCredentialsException("Invalid Header Value !!");
            }


            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(username);


                // Check if the user's password has been reset
                User userByEmail = userService.findByEmail(user.getUsername());

                Set<Role> roles = userByEmail.getRoles();
                boolean apiUserExists = roles.stream()
                        .anyMatch(role -> role.getName() == Roles.API_USER);

                if (apiUserExists) {
                    throw new BadCredentialsException("Unauthorized user.");
                }

                if(!request.getRequestURL().toString().endsWith("/api/v1/auth/change-password")){
                    if (userByEmail.getHasResetPassword()==Boolean.TRUE && userByEmail.getHasChangedPassword()==Boolean.FALSE) {
                        throw new BadCredentialsException("Password Reset. Please login");
                    }
                }

                if (userService.findByEmail(user.getUsername()).getStatus().equals(Status.INACTIVE)){
                    throw new BadCredentialsException("User is inactive.");
                }

                if (Boolean.TRUE.equals(jwtService.validateToken(token, user))) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    throw new BadCredentialsException("Invalid Token Provided");
                }
            }


        } catch (IllegalArgumentException e) {
            logger.error("Illegal Argument while fetching the username !!");
            request.setAttribute(AppConstants.EXCEPTION, e);
        } catch (ExpiredJwtException e) {
            logger.error("Given jwt token is expired !!");
            request.setAttribute(AppConstants.EXCEPTION, e);
        } catch (MalformedJwtException e) {
            logger.error("Some changes has been done in token !! Invalid Token");
            request.setAttribute(AppConstants.EXCEPTION, e);
        } catch (BadCredentialsException e) {
            logger.error(e.getMessage());
            request.setAttribute(AppConstants.EXCEPTION, e);
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
            request.setAttribute(AppConstants.EXCEPTION, ex);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestUrl = request.getRequestURL().toString();
        return requestUrl.endsWith("/api/v1/compress-api-user/compressFiles");
    }

}
