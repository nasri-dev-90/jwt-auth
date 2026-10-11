package com.example.jwtToken.Permission;

import com.example.jwtToken.action.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByResourceIdAndActionId(Long resourceid,Long actionid);
}