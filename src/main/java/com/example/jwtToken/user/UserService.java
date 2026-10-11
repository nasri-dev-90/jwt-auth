package com.example.jwtToken.user;

import com.example.jwtToken.Permission.Permission;
import com.example.jwtToken.Permission.PermissionRepository;
import com.example.jwtToken.ReqDtos.UpdateProfileRequest;
import com.example.jwtToken.action.Action;
import com.example.jwtToken.action.ActionRepository;
import com.example.jwtToken.resource.Resource;
import com.example.jwtToken.resource.ResourceRepository;
import com.example.jwtToken.role.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final ResourceRepository resourceRepository;
    private final ActionRepository actionRepository;
    private final PermissionRepository permissionRepository;
    private final RoleService roleService;

    @Transactional
    public User updateProfile(UpdateProfileRequest request) {
        // گرفتن کد ملی کاربر فعلی از کانتکست سکیوریتی
//        String nationalId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByNationalId(request.nationalId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        user.setAddress(request.address());
        user.setFatherName(request.fatherName());
        user.setBirthDate(request.birthDate());

        return userRepository.save(user);
    }

    public User findByNationalId(String id) {
        return userRepository.findByNationalId(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
