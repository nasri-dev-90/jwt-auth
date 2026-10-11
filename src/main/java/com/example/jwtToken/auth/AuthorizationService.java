package com.example.jwtToken.auth;

import com.example.jwtToken.resource.Resource;
import com.example.jwtToken.user.User;
import com.example.jwtToken.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("authz")
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;
    public Boolean checkAccess(String resource, String action) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        if(authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ALL_ALL") ||
                                grantedAuthority.getAuthority().equals("ROLE_SUPERADMIN")
                )){
            return true;
        }


        String requiredAuthority = resource + "_" + action;

        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(requiredAuthority));


    }

    public boolean isOwnerRresource(Long id, Resource resource) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // ۲. ساخت کوئری داینامیک JPQL
        // نکته: در JPQL نام کلاس جاوا استفاده می‌شود نه نام جدول دیتابیس
        String jpql = String.format(
                "SELECT COUNT(e) FROM %s e WHERE e.id = :id AND e.createdBy = :username",
                resource
        );

        try {
            // ۳. اجرای کوئری و بررسی وجود رکورد
            Long count = entityManager.createQuery(jpql, Long.class)
                    .setParameter("id", id)
                    .setParameter("username", currentUsername)
                    .getSingleResult();

            return count > 0; // اگر بزرگتر از صفر باشد یعنی رکورد مال این کاربر است

        } catch (Exception e) {
            // در صورت وجود خطا (مثلا نام Entity اشتباه باشد) دسترسی رد می‌شود
            return false;
        }
    }


    public boolean isOwnerUser(String id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return id.equals(currentUsername);


    }
}
