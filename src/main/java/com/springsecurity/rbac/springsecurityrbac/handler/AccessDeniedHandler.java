package com.springsecurity.rbac.springsecurityrbac.handler;

import com.springsecurity.rbac.springsecurityrbac.exception.UserAccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class AccessDeniedHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<UserAccessDeniedException> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {


        UserAccessDeniedException accessDeniedException = new UserAccessDeniedException(
                ex.getClass().toString(),
                ex.getMessage()+" or you don't have permission to perform this operation!",
                request.getDescription(false),
                LocalDateTime.now()

        );
        return new ResponseEntity<>(accessDeniedException, HttpStatus.FORBIDDEN);
    }

}