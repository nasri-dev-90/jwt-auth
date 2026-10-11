package com.example.jwtToken.role;

import com.example.jwtToken.ReqDtos.RolesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;


    @PostMapping
    public ResponseEntity<String> saveRoles(@RequestBody RolesRequest request) {
        String response = roleService.saveRoles(request);

        return ResponseEntity.ok(response);
    }
}
