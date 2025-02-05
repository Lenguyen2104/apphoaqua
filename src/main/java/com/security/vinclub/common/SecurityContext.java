package com.security.vinclub.common;

import com.security.vinclub.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityContext {
    public static User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                return (User) authentication.getPrincipal();
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    public static String getCurrentUserId() {
        return SecurityContext.getCurrentUser().getId().toString();
    }
}
