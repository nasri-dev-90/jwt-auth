package com.example.jwtToken.user;

import com.example.jwtToken.ReqDtos.RoleListToUserDto;
import com.example.jwtToken.ReqDtos.UpdateProfileRequest;
import com.example.jwtToken.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;


    @GetMapping("/{nationalId}")
    @PreAuthorize("@authz.checkAccess('USER', 'DISPLAY') OR @authz.isOwnerUser(#nationalId)" )
    public ResponseEntity<User> getUserProfile(
            @PathVariable String nationalId,
            @AuthenticationPrincipal User currentUser) {
        User user=userService.findByNationalId(nationalId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateProfileRequest request) {
        userService.updateProfile(request);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @PostMapping("/setroles")
//    @PreAuthorize("@authz.checkAccess('USER','UPDATE')")
    public ResponseEntity<User> setUserRoles(@RequestBody RoleListToUserDto request){

        for (String roleName :request.roles()){
            roleService.assignRoleToUser(request.nationalId(),roleName);
        }
        User user=userService.findByNationalId(request.nationalId());
        return ResponseEntity.ok(user);
    }

}