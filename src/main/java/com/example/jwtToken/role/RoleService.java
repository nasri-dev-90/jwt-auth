package com.example.jwtToken.role;

import com.example.jwtToken.action.Action;
import com.example.jwtToken.Permission.Permission;
import com.example.jwtToken.Permission.PermissionRepository;
import com.example.jwtToken.action.ActionRepository;
import com.example.jwtToken.resource.Resource;
import com.example.jwtToken.ReqDtos.PermissionsDto;
import com.example.jwtToken.ReqDtos.RolesRequest;
import com.example.jwtToken.resource.ResourceRepository;
import com.example.jwtToken.user.User;
import com.example.jwtToken.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;
    private final ResourceRepository resourceRepository;
    private final ActionRepository actionRepository;
    private final UserRepository userRepository;

    @Transactional
    public String saveRoles(RolesRequest request) {

        boolean existsRole = roleRepository.findByName(request.name().toUpperCase()).isPresent();
        Role newRole = roleRepository.findByName(request.name().toUpperCase()).orElseGet(() -> {

            Role role = new Role();
            role.setName(request.name().toUpperCase());
            Set<Permission> rolePermissions = new HashSet<>();
            for (PermissionsDto permission : request.permissions()) {
                Resource resource = resourceRepository
                        .findByName(permission.resource().toUpperCase())
                        .orElseGet(() -> {
                            Resource newResource = new Resource();
                            newResource.setName(permission.resource().toUpperCase());
                            return resourceRepository.save(newResource);
                        });

                Action action = actionRepository
                        .findByName(permission.action().toUpperCase())
                        .orElseGet(() -> {
                            Action newAction = new Action();
                            newAction.setName(permission.action().toUpperCase());
                            return actionRepository.save(newAction);
                        });

                Permission sevedPermission = permissionRepository
                        .findByResourceIdAndActionId(resource.getId(), action.getId())
                        .orElseGet(() -> {
                            Permission newPermission = new Permission();
                            newPermission.setResource(resource);
                            newPermission.setAction(action);
                            return permissionRepository.save(newPermission);
                        });

                rolePermissions.add(sevedPermission);
            }

            role.setPermissions(rolePermissions);
            role.setName(request.name().toUpperCase());
            return roleRepository.save(role);

        });

        if (existsRole) {

        return  " role " + request.name() + " uppdate successfully.";
        }else {
            return  " role " + request.name() + " created successfully.";
        }

    }


    @Transactional
    public void assignRoleToUser(String nationalId, String roleName) {
        User user = userRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new RuntimeException("کاربر یافت نشد"));

        Role role = roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new RuntimeException("نقش یافت نشد"));

        // افزودن نقش به لیست نقش‌های کاربر
        user.getRoles().add(role);

        // نیازی به فراخوانی userRepository.save(user) نیست!
        // به خاطر @Transactional، هایبرنیت خودش تغییرات را در دیتابیس ثبت می‌کند.
    }

    /**
     * حذف یک نقش از کاربر
     */
    @Transactional
    public void revokeRoleFromUser(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("کاربر یافت نشد"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("نقش یافت نشد"));

        user.getRoles().remove(role);
    }

    /**
     * تخصیص یک دسترسی مستقیم (استثنایی) به کاربر
     */
//    @Transactional
//    public void assignDirectPermissionToUser(Long userId, String resourceName, String actionName) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("کاربر یافت نشد"));
//
////        Permission permission = permissionRepository.findByResourceNameAndActionName(resourceName, actionName)
////                .orElseThrow(() -> new RuntimeException("دسترسی مورد نظر در سیستم تعریف نشده است"));
//
//        // افزودن پرمیشن به لیست پرمیشن‌های مستقیم کاربر
//        user.getDirectPermissions().add(permission);
//    }

    /**
     * تخصیص گروهی نقش‌ها و دسترسی‌ها به کاربر (مثلاً هنگام ویرایش پروفایل کاربر توسط ادمین)
     */
    @Transactional
    public void updateAllUserAccess(Long userId, Set<String> roleNames, Set<Long> directPermissionIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("کاربر یافت نشد"));

        // بروزرسانی نقش‌ها
        if (roleNames != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findByNameIn(roleNames));
            user.setRoles(roles); // جایگزینی کامل نقش‌های قبلی با نقش‌های جدید
        }

        // بروزرسانی پرمیشن‌های مستقیم
        if (directPermissionIds != null) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(directPermissionIds));
            user.setDirectPermissions(permissions);
        }
    }


}