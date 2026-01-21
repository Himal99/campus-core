package com.sb.file.compressor.auth.config;

import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.entity.Role;
import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.service.contract.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

/**
 * @author prabesh on 05/10/24
 */
@Aspect
@Component
public class RequiredPermissionAspect {
    private static final Logger logger = LoggerFactory.getLogger(RequiredPermissionAspect.class);



    private final UserService loggedInService;

    public RequiredPermissionAspect(UserService loggedInService) {
        this.loggedInService = loggedInService;
    }

    @Around("@annotation(com.sb.file.compressor.auth.config.RequiredPermission)")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        RequiredPermission permission = this.getAnnotation(joinPoint);

        if (permission.value().length == 0) {
            throw new BadCredentialsException("Unauthorized User");
        }

        User userDetail = this.loggedInService.authenticatedUser();

        if (!this.hasPermission(userDetail, permission)) {
            throw new BadCredentialsException("Unauthorized User");
        }


        return joinPoint.proceed();
    }


    private RequiredPermission getAnnotation(ProceedingJoinPoint context) {

        //annotated method starts here
        MethodSignature signature = (MethodSignature) context.getSignature();
        RequiredPermission requiredPermission = signature.getMethod().getAnnotation(RequiredPermission.class);

        if (requiredPermission == null) {
            requiredPermission = signature.getMethod().getDeclaringClass().getAnnotation(RequiredPermission.class);
        }
        return requiredPermission;
    }

    private boolean hasPermission(User currentUserDetail, RequiredPermission requiredPermission) {
        Set<Role> roles = currentUserDetail.getRoles();
        if (roles ==  null || roles.isEmpty() ){
            throw new BadCredentialsException("Unauthorized user");
        }
        return Arrays.stream(requiredPermission.value()).anyMatch(permission -> roles.stream().map(role -> role.getName().getValue()).toList().contains(permission));

    }

}
